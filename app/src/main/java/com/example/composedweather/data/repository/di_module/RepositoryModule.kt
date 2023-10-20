package com.example.composedweather.data.repository.di_module

import com.example.composedweather.core.local.PreferencesDataStore
import com.example.composedweather.data.remote.LocationRemoteDataSource
import com.example.composedweather.data.remote.WeatherRemoteDataSource
import com.example.composedweather.data.repository.contract.LocationRepository
import com.example.composedweather.data.repository.contract.UserPreferenceRepository
import com.example.composedweather.data.repository.contract.WeatherRepository
import com.example.composedweather.data.repository.implementation.LocationRepositoryImpl
import com.example.composedweather.data.repository.implementation.UserPreferenceRepositoryImpl
import com.example.composedweather.data.repository.implementation.WeatherRepositoryImpl
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