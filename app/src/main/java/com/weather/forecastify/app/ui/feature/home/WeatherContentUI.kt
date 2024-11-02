package com.weather.forecastify.app.ui.feature.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.weather.forecastify.data.model.response.Current
import com.weather.forecastify.data.model.response.CurrentUnits
import com.weather.forecastify.data.model.response.DailyForecast
import com.weather.forecastify.data.model.response.HourlyForecast

@Composable
fun WeatherContentUI(
    state: HomeViewState,
    hourlyForecasts: SnapshotStateList<HourlyForecast>,
    dailyForecasts: SnapshotStateList<DailyForecast>,
    todayWeather: Pair<Current, CurrentUnits>?,
    modifyContent: (HomeViewState) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize(),
    ) {

        var showTodaysInfo by rememberSaveable {
            mutableStateOf(true)
        }

        var showHourlyForecast by rememberSaveable {
            mutableStateOf(true)
        }

        Filters(
            state = state,
            showTodaysInfo = showTodaysInfo,
            showHourlyForecast = showHourlyForecast,
            toggleTodaysWeatherVisibility = {
                showTodaysInfo = showTodaysInfo.not()
            },
            toggleHourlyForecast = {
                showHourlyForecast = showHourlyForecast.not()
            },
            modifyContent = modifyContent
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(vertical = 12.dp)
        ) {
            item(
                key = "TodaysWeatherInfo",
                contentType = {
                    "TodaysWeatherContent"
                }
            ) {
                AnimatedVisibility(
                    visible = (todayWeather != null && showTodaysInfo),
                    label = "Today Weather Header"
                ) {
                    todayWeather?.let {
                        TodayWeatherItem(it)
                    }
                }
            }

            item(
                key = "HourlyForecast",
                contentType = {
                    "HourlyForecastRow"
                }
            ) {
                AnimatedVisibility(
                    visible = showHourlyForecast,
                    label = "Hourly Forecast Row"
                ) {
                    HourlyForecastRow(
                        hourlyForecasts = hourlyForecasts,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            }

            item(
                key = "WeeklyForecastHeader123",
                contentType = {
                    "Weekly Forecast Header"
                }
            ) {
                AnimatedVisibility(visible = dailyForecasts.isNotEmpty()) {
                    Text(
                        text = "Weekly Forecast",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(6.dp)
                    )
                }
            }
            items(
                count = dailyForecasts.size,
                key = { index ->
                    dailyForecasts[index].time + index
                },
                contentType = {
                    "Weekly Forecast Info"
                }
            ) { index ->
                DailyWeatherItem(dailyForecasts[index])
            }
        }
    }
}
