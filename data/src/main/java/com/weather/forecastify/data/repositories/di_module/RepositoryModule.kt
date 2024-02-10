package com.weather.forecastify.data.repositories.di_module

import com.weather.forecastify.data.local.PreferencesDataStore
import com.weather.forecastify.data.remote.LocationRemoteDataSource
import com.weather.forecastify.data.remote.WeatherRemoteDataSource
import com.weather.forecastify.data.repositories.contract.LocationRepository
import com.weather.forecastify.data.repositories.contract.UserPreferenceRepository
import com.weather.forecastify.data.repositories.contract.WeatherRepository
import com.weather.forecastify.data.repositories.implementation.LocationRepositoryImpl
import com.weather.forecastify.data.repositories.implementation.UserPreferenceRepositoryImpl
import com.weather.forecastify.data.repositories.implementation.WeatherRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Singleton
    @Provides
    fun provideWeatherRepository(
        remoteDataSource: WeatherRemoteDataSource
    ): WeatherRepository {
        return WeatherRepositoryImpl(
            remoteDataSource = remoteDataSource
        )
    }

    @Provides
    @Singleton
    fun provideUserPreferenceRepository(
        preferencesDataStore: PreferencesDataStore,
    ): UserPreferenceRepository {
        return UserPreferenceRepositoryImpl(
            dataStore = preferencesDataStore,
        )
    }

    @Provides
    @Singleton
    fun provideLocationRepository(
        remoteDataSource: LocationRemoteDataSource,
    ): LocationRepository {
        return LocationRepositoryImpl(
            remoteDataSource = remoteDataSource
        )
    }

}