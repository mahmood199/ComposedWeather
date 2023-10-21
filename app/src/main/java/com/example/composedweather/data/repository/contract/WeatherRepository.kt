package com.example.composedweather.data.repository.contract

import com.example.core_network.NetworkResult
import com.example.composedweather.data.models.request.WeatherDataRequest
import com.example.composedweather.data.models.response.WeatherResponse

interface WeatherRepository {

    suspend fun getInfo(weatherDataRequest: WeatherDataRequest): NetworkResult<WeatherResponse>

}