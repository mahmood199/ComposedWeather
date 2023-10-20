package com.example.composedweather.data.repository.implementation

import com.example.composedweather.core.remote.NetworkResult
import com.example.composedweather.data.models.response.LocationResponseItem
import com.example.composedweather.data.remote.LocationRemoteDataSource
import com.example.composedweather.data.repository.contract.LocationRepository
import javax.inject.Inject

class LocationRepositoryImpl @Inject constructor(
    private val remoteDataSource: LocationRemoteDataSource
): LocationRepository {

    override suspend fun searchByName(location: String): NetworkResult<List<LocationResponseItem>> {
        return remoteDataSource.getLocations(location)
    }

}