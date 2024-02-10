package com.weather.forecastify.domain.location

import com.weather.forecastify.core_network.NetworkResult
import com.weather.forecastify.data.model.response.LocationResponseItem
import com.weather.forecastify.data.repositories.contract.LocationRepository
import javax.inject.Inject

class LocationUseCase @Inject constructor(
    private val repository: LocationRepository,
) {

    suspend fun searchByName(location: String): NetworkResult<List<LocationResponseItem>> {
        return repository.searchByName(location)
    }

}