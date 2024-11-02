package com.weather.forecastify.app.ui.feature.home

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.PermissionStatus
import com.weather.forecastify.app.ui.common.ContentLoaderUI
import com.weather.forecastify.app.ui.common.SaveableLaunchedEffect
import com.weather.forecastify.data.model.response.Current
import com.weather.forecastify.data.model.response.CurrentUnits
import com.weather.forecastify.data.model.response.DailyForecast
import com.weather.forecastify.data.model.response.HourlyForecast
import java.text.DecimalFormat

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HomeUiContent(
    state: HomeViewState,
    permissionState: PermissionState,
    dailyForecasts: SnapshotStateList<DailyForecast>,
    hourlyForecasts: SnapshotStateList<HourlyForecast>,
    todayWeather: Pair<Current, CurrentUnits>?,
    modifyContent: (HomeViewState) -> Unit,
    onPermissionGranted: () -> Unit,
    modifier: Modifier = Modifier
) {
    SaveableLaunchedEffect(Unit) {
        permissionState.launchPermissionRequest()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        val latitude = remember(state.weatherDataRequest.latitude) {
            DecimalFormat("#.##").format(state.weatherDataRequest.latitude)
        }

        val longitude = remember(state.weatherDataRequest.longitude) {
            DecimalFormat("#.##").format(state.weatherDataRequest.longitude)
        }

        val textToShow = remember(latitude, longitude, permissionState) {
            if ((latitude.toDoubleOrNull() ?: 0.0) == 0.0 &&
                (longitude.toDoubleOrNull() ?: 0.0) == 0.0
            )
                "Please grant location access from settings or search for your location"
            else "Showing forecasts for Lat:- $latitude, Lon:- $longitude"
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20))
                .border(BorderStroke(4.dp, MaterialTheme.colorScheme.onPrimary))
                .padding(16.dp)
        ) {
            Text(
                text = textToShow,
                maxLines = 1,
                modifier = Modifier
                    .fillMaxWidth()
                    .basicMarquee(100)
            )
        }

        when (permissionState.status) {
            is PermissionStatus.Denied -> {
                RequestPermissionUI()
            }

            PermissionStatus.Granted -> {
                SaveableLaunchedEffect(state.weatherDataRequest.isLocationDetected) {
                    if (state.weatherDataRequest.isLocationDetected) {
                        onPermissionGranted()
                    }
                }

                AnimatedContent(
                    targetState = state.isLoading,
                    label = "Forecast loading"
                ) { isLoading ->
                    if (isLoading) {
                        ContentLoaderUI()
                    } else {
                        WeatherContentUI(
                            state = state,
                            hourlyForecasts = hourlyForecasts,
                            dailyForecasts = dailyForecasts,
                            modifyContent = modifyContent,
                            todayWeather = todayWeather
                        )
                    }
                }
            }
        }
    }
}
