package com.example.data.repositories.implementation

import com.example.core_network.NetworkResult
import com.example.data.model.response.LocationResponseItem
import com.example.data.remote.LocationRemoteDataSource
import com.example.data.repositories.contract.LocationRepository
import javax.inject.Inject

class LocationRepositoryImpl @Inject constructor(
    private val remoteDataSource: LocationRemoteDataSource
): LocationRepository {

    override suspend fun searchByName(location: String): NetworkResult<List<LocationResponseItem>> {
        return remoteDataSource.getLocations(location)
    }

}