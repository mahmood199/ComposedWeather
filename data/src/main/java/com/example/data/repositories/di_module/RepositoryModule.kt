package com.example.data.repositories.di_module

import com.example.data.local.PreferencesDataStore
import com.example.data.remote.LocationRemoteDataSource
import com.example.data.remote.WeatherRemoteDataSource
import com.example.data.repositories.contract.LocationRepository
import com.example.data.repositories.contract.UserPreferenceRepository
import com.example.data.repositories.contract.WeatherRepository
import com.example.data.repositories.implementation.LocationRepositoryImpl
import com.example.data.repositories.implementation.UserPreferenceRepositoryImpl
import com.example.data.repositories.implementation.WeatherRepositoryImpl
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