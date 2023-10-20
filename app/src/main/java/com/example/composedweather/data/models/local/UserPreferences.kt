package com.example.composedweather.data.models.local

data class UserPreferences(
    val latitude: Double,
    val longitude: Double,
    val temperatureUnit: String,
    val location: String,
    val isLocationDetected: Boolean
)