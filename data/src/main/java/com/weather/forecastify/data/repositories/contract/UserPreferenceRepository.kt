package com.weather.forecastify.data.repositories.contract

import com.weather.forecastify.data.model.local.UserPreferences
import kotlinx.coroutines.flow.Flow

interface UserPreferenceRepository {

    fun getUserPreferences(): Flow<UserPreferences>

    suspend fun setTemperatureUnit(temperatureUnit: String)

    suspend fun setUserLocation(
        latitude: Double,
        longitude: Double,
        location: String,
        isLocationDetected: Boolean
    )
}