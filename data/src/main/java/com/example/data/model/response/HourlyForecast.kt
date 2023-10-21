package com.example.data.model.response

data class HourlyForecast(
    val relativeHumidity: Int,
    val relativeHumidityUnit: String,
    val temperature: Double,
    val temperatureUnit: String,
    val time: String,
    val timeUnit: String,
)