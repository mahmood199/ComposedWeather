package com.example.composedweather.core.local

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.composedweather.data.models.local.UserPreferences
import com.example.composedweather.data.models.request.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

class PreferencesDataStore @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {

    val userPreferencesFlow: Flow<UserPreferences> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                Log.e("PreferencesDataStore", exception.toString())
                emit(emptyPreferences())
            } else {
                throw Exception()
            }
        }
        .map { preferences ->
            val latitude = preferences[PreferencesKeys.LATITUDE] ?: 0.0
            val longitude = preferences[PreferencesKeys.LONGITUDE] ?: 0.0
            val temperatureUnit = preferences[PreferencesKeys.TEMPERATURE_UNIT] ?: Constants.CELSIUS
            val locationName = preferences[PreferencesKeys.LOCATION_NAME] ?: ""
            val isLocationAutoDetected = preferences[PreferencesKeys.IS_LOCATION_AUTO_DETECTED] ?: true

            UserPreferences(
                latitude = latitude,
                longitude = longitude,
                temperatureUnit = temperatureUnit,
                location = locationName,
                isLocationDetected = isLocationAutoDetected
            )
        }

    suspend fun setUserLocation(latitude: Double, longitude: Double, userLocation: String, isLocationDetected: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.LATITUDE] = latitude
            preferences[PreferencesKeys.LONGITUDE] = longitude
            preferences[PreferencesKeys.LOCATION_NAME] = userLocation
            preferences[PreferencesKeys.IS_LOCATION_AUTO_DETECTED] = isLocationDetected
        }
    }

    suspend fun setTemperatureUnit(temperatureUnit: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.TEMPERATURE_UNIT] = temperatureUnit
        }
    }

    private object PreferencesKeys {
        val LATITUDE = doublePreferencesKey("latitude")
        val LONGITUDE = doublePreferencesKey("longitude")
        val TEMPERATURE_UNIT = stringPreferencesKey("temperature_unit")
        val LOCATION_NAME = stringPreferencesKey("location_name")
        val IS_LOCATION_AUTO_DETECTED = booleanPreferencesKey("is_location_auto_detected")
    }

    companion object {
        const val USER_PREFERENCES_NAME = "user_preferences"
    }

}