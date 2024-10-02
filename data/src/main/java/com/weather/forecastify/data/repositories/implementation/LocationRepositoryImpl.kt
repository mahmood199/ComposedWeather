package com.weather.forecastify.data.repositories.implementation

import com.weather.forecastify.core_network.NetworkResult
import com.weather.forecastify.data.model.response.LocationResponseItem
import com.weather.forecastify.data.remote.LocationRemoteDataSource
import com.weather.forecastify.data.repositories.contract.LocationRepository
import javax.inject.Inject

class LocationRepositoryImpl @Inject constructor(
    private val remoteDataSource: LocationRemoteDataSource
) : LocationRepository {

    override suspend fun searchByName(location: String): NetworkResult<List<LocationResponseItem>> {
        return remoteDataSource.getLocations(location)
    }

}