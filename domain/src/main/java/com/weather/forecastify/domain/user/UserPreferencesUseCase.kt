package com.weather.forecastify.domain.user

import com.weather.forecastify.data.model.local.UserPreferences
import com.weather.forecastify.data.repositories.contract.UserPreferenceRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserPreferencesUseCase @Inject constructor(
    private val repository: UserPreferenceRepository
) {

    fun getUserPreferences(): Flow<UserPreferences> {
        return repository.getUserPreferences()
    }

    suspend fun setTemperatureUnit(temperatureUnit: String) {
        return repository.setTemperatureUnit(temperatureUnit = temperatureUnit)
    }

    suspend fun setUserLocation(
        latitude: Double,
        longitude: Double,
        location: String,
        isLocationDetected: Boolean
    ) {
        repository.setUserLocation(
            latitude = latitude,
            longitude = longitude,
            location = location,
            isLocationDetected = isLocationDetected
        )
    }

}