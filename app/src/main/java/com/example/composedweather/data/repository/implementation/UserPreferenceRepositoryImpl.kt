package com.example.composedweather.data.repository.implementation

import com.example.composedweather.core.local.PreferencesDataStore
import com.example.composedweather.data.models.local.UserPreferences
import com.example.composedweather.data.repository.contract.UserPreferenceRepository
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