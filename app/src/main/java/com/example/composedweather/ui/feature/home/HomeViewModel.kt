package com.example.composedweather.ui.feature.home

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composedweather.connection.NetworkConnectivityObserver
import com.example.composedweather.core.remote.NetworkResult
import com.example.composedweather.data.models.request.Constants
import com.example.composedweather.data.models.response.Current
import com.example.composedweather.data.models.response.CurrentUnits
import com.example.composedweather.data.models.response.Daily
import com.example.composedweather.data.models.response.DailyUnits
import com.example.composedweather.data.models.response.DailyForecast
import com.example.composedweather.data.models.response.Hourly
import com.example.composedweather.data.models.response.HourlyForecast
import com.example.composedweather.data.models.response.HourlyUnits
import com.example.composedweather.data.repository.contract.UserPreferenceRepository
import com.example.composedweather.data.repository.contract.WeatherRepository
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
    private val weatherRepository: WeatherRepository,
    private val userPreferenceRepository: UserPreferenceRepository
) : ViewModel() {

    private val _state = MutableStateFlow(HomeViewState())
    val state = _state.asStateFlow()

    val dailyForecasts = mutableStateListOf<DailyForecast>()
    val hourlyForecasts = mutableStateListOf<HourlyForecast>()

    private val _currrentDayWeather: MutableStateFlow<Pair<Current, CurrentUnits>?> =
        MutableStateFlow(null)
    val currentDayWeather = _currrentDayWeather.asStateFlow()


    init {
        viewModelScope.launch {
            networkConnectivityObserver.networkState.collectLatest {
                _state.value = _state.value.copy(isConnected = it)
            }
        }

        viewModelScope.launch {
            userPreferenceRepository.getUserPreferences().distinctUntilChanged().collectLatest {
                val request = _state.value.weatherDataRequest

                _state.value = _state.value.copy(
                    weatherDataRequest = request.copy(
                        latitude = it.latitude,
                        longitude = it.longitude,
                        temperatureUnit = it.temperatureUnit,
                        isLocationDetected = it.isLocationDetected
                    )
                )

                _state.value = _state.value.copy(
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
            _state.value = _state.value.copy(isLoading = true)
            when (val result = weatherRepository.getInfo(state.value.weatherDataRequest)) {
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
                    getDayWiseForecast(result.data.daily, result.data.dailyUnits)
                    getWeatherForToday(result.data.current, result.data.currentUnits)
                    getHourlyForecasts(result.data.hourly, result.data.hourlyUnits)

                    _state.value = _state.value.copy(isLoading = false)
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

    private fun handleError(e: Throwable) {
        _state.value = _state.value.copy(error = e.message)
        _state.value = _state.value.copy(isLoading = false)
    }

    fun modifyState(state: HomeViewState) {
        viewModelScope.launch(Dispatchers.IO) {
            userPreferenceRepository.setTemperatureUnit(state.weatherDataRequest.temperatureUnit)
        }
    }

    fun setLocationCoordinates(latitude: Double, longitude: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            userPreferenceRepository.setUserLocation(
                latitude = latitude,
                longitude = longitude,
                location = "Your current location",
                isLocationDetected = true
            )
        }
    }

    fun handleLocationError() {
        _state.value = _state.value.copy(error = "Unable to fetch location")
    }


}