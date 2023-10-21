package com.example.data.model.request


data class WeatherDataRequest(
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val params: List<String> = emptyList(),
    val temperatureUnit: String = Constants.CELSIUS,
    val forecastDays: Int = 10,
    val pastDays: Int = 0,
    val startDate: String = "",
    val endDate: String = "",
    val currentDayRequested: Boolean = true,
    val isLocationDetected: Boolean = false,
    val isHourlyDataRequested: Boolean = false
)
