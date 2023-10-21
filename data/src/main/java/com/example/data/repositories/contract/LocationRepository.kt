package com.example.data.repositories.contract

import com.example.core_network.NetworkResult
import com.example.data.model.response.LocationResponseItem

interface LocationRepository {

    suspend fun searchByName(location: String): NetworkResult<List<LocationResponseItem>>

}