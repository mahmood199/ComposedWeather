package com.example.domain.location

import com.example.core_network.NetworkResult
import com.example.data.model.response.LocationResponseItem
import com.example.data.repositories.contract.LocationRepository
import javax.inject.Inject

class LocationUseCase @Inject constructor(
    private val repository: LocationRepository,
) {

    suspend fun searchByName(location: String): NetworkResult<List<LocationResponseItem>> {
        return repository.searchByName(location)
    }

}