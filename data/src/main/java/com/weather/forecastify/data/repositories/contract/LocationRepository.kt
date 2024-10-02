package com.weather.forecastify.data.repositories.contract

import com.weather.forecastify.core_network.NetworkResult
import com.weather.forecastify.data.model.response.LocationResponseItem

interface LocationRepository {

    suspend fun searchByName(location: String): NetworkResult<List<LocationResponseItem>>

}