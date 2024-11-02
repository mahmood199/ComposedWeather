package com.weather.forecastify.app.ui.feature.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.yml.charts.ui.linechart.LineChart
import com.weather.forecastify.data.model.response.Hourly
import com.weather.forecastify.data.model.response.HourlyForecast


@Composable
fun HourlyForecastRow(
    hourlyForecasts: SnapshotStateList<HourlyForecast>,
    modifier: Modifier = Modifier
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        item(key = "HourlyForeCast Start Item") {
            AnimatedVisibility(hourlyForecasts.isNotEmpty()) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text("Time")
                    Text("Temperature")
                    Text("Humidity")
                }
            }
        }

        item("hourly_data_chart") {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillParentMaxWidth()
            ) {
                if (hourlyForecasts.isNotEmpty()) {
                    LineChart(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(ratio = 1.5f),
                        lineChartData = getHourlyChartData(hourlyForecasts)
                    )
                }
            }
        }

        items(
            count = hourlyForecasts.size,
            key = { index ->
                hourlyForecasts[index].time +
                        hourlyForecasts[index].temperature +
                        hourlyForecasts[index].relativeHumidity
            }, contentType = {
                "Hourly Forecast Item"
            }
        ) { index ->
            HourlyForecastItem(
                hourlyForecast = hourlyForecasts[index],
            )
        }
    }
}

@Preview
@Composable
private fun HourlyForecastRowPreview() {
    Box(
        modifier = Modifier.fillMaxSize().background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Text("sadfgbbdsadfv")
        if (LocalInspectionMode.current) {
            Text("LocalInspectionMode.current")
        }
    }
}