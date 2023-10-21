package com.example.data.repositories.contract

import com.example.core_network.NetworkResult
import com.example.data.model.request.WeatherDataRequest
import com.example.data.model.response.WeatherResponse

interface WeatherRepository {

    suspend fun getInfo(weatherDataRequest: WeatherDataRequest): NetworkResult<WeatherResponse>

}