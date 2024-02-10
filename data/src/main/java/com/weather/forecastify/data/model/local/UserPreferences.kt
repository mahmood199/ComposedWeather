package com.weather.forecastify.data.model.local

data class UserPreferences(
    val latitude: Double,
    val longitude: Double,
    val temperatureUnit: String,
    val location: String,
    val isLocationDetected: Boolean
)