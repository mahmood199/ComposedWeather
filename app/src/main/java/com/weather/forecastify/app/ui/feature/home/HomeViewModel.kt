package com.weather.forecastify.app.ui.feature.home

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weather.forecastify.app.ui.state.StateManager
import com.weather.forecastify.connectivity_android.NetworkConnectivityObserver
import com.weather.forecastify.data.model.request.Constants
import com.weather.forecastify.data.model.response.Current
import com.weather.forecastify.data.model.response.CurrentUnits
import com.weather.forecastify.data.model.response.Daily
import com.weather.forecastify.data.model.response.DailyForecast
import com.weather.forecastify.data.model.response.DailyUnits
import com.weather.forecastify.data.model.response.Hourly
import com.weather.forecastify.data.model.response.HourlyForecast
import com.weather.forecastify.data.model.response.HourlyUnits
import com.weather.forecastify.core_network.NetworkResult
import com.weather.forecastify.domain.user.UserPreferencesUseCase
import com.weather.forecastify.domain.weather.WeatherInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val networkConnectivityObserver: NetworkConnectivityObserver,
    private val weatherInfoUseCase: WeatherInfoUseCase,
    private val userPreferencesUseCase: UserPreferencesUseCase
) : ViewModel(), StateManager<HomeViewState> {

    override val _state = MutableStateFlow(HomeViewState.defaultState())
    override val state = _state.asStateFlow()

    val dailyForecasts = mutableStateListOf<DailyForecast>()
    val hourlyForecasts = mutableStateListOf<HourlyForecast>()

    private val _currrentDayWeather: MutableStateFlow<Pair<Current, CurrentUnits>?> =
        MutableStateFlow(null)
    val currentDayWeather = _currrentDayWeather.asStateFlow()


    init {
        viewModelScope.launch {
            networkConnectivityObserver.networkState.collectLatest {
                updateState {
                    copy(isConnected = it)
                }
            }
        }

        viewModelScope.launch {
            userPreferencesUseCase.getUserPreferences().distinctUntilChanged().collectLatest {
                val request = _state.value.weatherDataRequest

                updateState {
                    copy(
                        weatherDataRequest = request.copy(
                            latitude = it.latitude,
                            longitude = it.longitude,
                            temperatureUnit = it.temperatureUnit,
                            isLocationDetected = it.isLocationDetected
                        )
                    )
                }

                updateState {
                    copy(
                        weatherDataRequest = _state.value.weatherDataRequest.copy(
                            params = listOf(
                                Constants.APPARENT_TEMPERATURE,
                                Constants.IS_DAY,
                                Constants.PRECIPITATION,
                                Constants.RAIN,
                                Constants.RELATIVE_HUMIDITY_2M,
                                Constants.SHOWERS,
                                Constants.TEMPERATURE_2M,
                                Constants.DEW_POINT_2M,
                                Constants.WIND_SPEED_10M,
                                Constants.WIND_DIRECTION_10M,
                            ), isHourlyDataRequested = true
                        ),
                    )
                }


                if (_state.value.weatherDataRequest.latitude != 0.0 &&
                    _state.value.weatherDataRequest.longitude != 0.0
                ) {
                    getInfo()
                }
            }
        }
    }

    private fun getInfo() {
        viewModelScope.launch(Dispatchers.IO) {
            updateState {
                copy(isLoading = true)
            }
            when (val result = weatherInfoUseCase.getInfo(state.value.weatherDataRequest)) {
                is NetworkResult.Exception -> {
                    handleError(result.e)
                }

                is NetworkResult.RedirectError -> {
                    handleError(result.e)
                }

                is NetworkResult.ServerError -> {
                    handleError(result.e)
                }

                is NetworkResult.UnAuthorised -> {
                    handleError(result.e)
                }

                is NetworkResult.Success -> {
                    getDayWiseForecast(
                        daily = result.data.daily,
                        dailyUnits = result.data.dailyUnits
                    )
                    getWeatherForToday(
                        current = result.data.current,
                        currentUnits = result.data.currentUnits
                    )
                    getHourlyForecasts(
                        hourly = result.data.hourly,
                        hourlyUnits = result.data.hourlyUnits
                    )

                    updateState {
                        copy(isLoading = false)
                    }
                }
            }
        }
    }

    private fun getHourlyForecasts(hourly: Hourly, hourlyUnits: HourlyUnits) {
        hourlyForecasts.clear()
        // Taking only 24 hours forecast
        hourly.time.take(24).forEachIndexed { index, s ->
            hourlyForecasts.add(
                HourlyForecast(
                    hourly.relativeHumidity2m[index], hourlyUnits.relativeHumidity2m,
                    hourly.temperature2m[index], hourlyUnits.temperature2m,
                    hourly.time[index], hourlyUnits.time,
                )
            )
        }
    }

    private fun getWeatherForToday(current: Current, currentUnits: CurrentUnits) {
        _currrentDayWeather.value = Pair(current, currentUnits)
    }

    private fun getDayWiseForecast(daily: Daily, dailyUnits: DailyUnits) {
        dailyForecasts.clear()
        daily.time.forEachIndexed { index, _ ->
            val dailyForecast = DailyForecast(
                precipitationSum = daily.precipitationSum[index],
                precipitationSumUnit = dailyUnits.precipitationSum,
                precipitationHours = daily.precipitationHours[index],
                precipitationHoursUnit = dailyUnits.precipitationHours,
                temperature2mMax = daily.temperature2mMax[index],
                temperature2mMaxUnit = dailyUnits.temperature2mMax,
                temperature2mMin = daily.temperature2mMin[index],
                temperature2mMinUnit = dailyUnits.temperature2mMin,
                time = daily.time[index],
                timeUnit = dailyUnits.time
            )
            dailyForecasts.add(dailyForecast)
        }
    }

    private suspend fun handleError(e: Throwable) {
        updateState {
            copy(
                error = e.message,
                isLoading = false
            )
        }
    }

    fun modifyState(state: HomeViewState) {
        viewModelScope.launch(Dispatchers.IO) {
            userPreferencesUseCase.setTemperatureUnit(state.weatherDataRequest.temperatureUnit)
        }
    }

    fun setLocationCoordinates(latitude: Double, longitude: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            userPreferencesUseCase.setUserLocation(
                latitude = latitude,
                longitude = longitude,
                location = "Your current location",
                isLocationDetected = true
            )
        }
    }

    fun handleLocationError() {
        viewModelScope.launch {
            updateState {
                copy(error = "Unable to fetch location")
            }
        }
    }


}