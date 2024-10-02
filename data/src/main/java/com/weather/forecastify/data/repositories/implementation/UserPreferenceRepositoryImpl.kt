package com.weather.forecastify.data.repositories.implementation

import com.weather.forecastify.data.local.PreferencesDataStore
import com.weather.forecastify.data.model.local.UserPreferences
import com.weather.forecastify.data.repositories.contract.UserPreferenceRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserPreferenceRepositoryImpl @Inject constructor(
    private val dataStore: PreferencesDataStore,
) : UserPreferenceRepository {

    override fun getUserPreferences(): Flow<UserPreferences> {
        return dataStore.userPreferencesFlow
    }

    override suspend fun setUserLocation(
        latitude: Double,
        longitude: Double,
        location: String,
        isLocationDetected: Boolean
    ) {
        dataStore.setUserLocation(
            latitude = latitude,
            longitude = longitude,
            userLocation = location,
            isLocationDetected = isLocationDetected
        )
    }

    override suspend fun setTemperatureUnit(temperatureUnit: String) {
        dataStore.setTemperatureUnit(temperatureUnit = temperatureUnit)
    }

}