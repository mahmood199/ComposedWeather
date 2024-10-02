package com.weather.forecastify.data.repositories.contract

import com.weather.forecastify.core_network.NetworkResult
import com.weather.forecastify.data.model.request.WeatherDataRequest
import com.weather.forecastify.data.model.response.WeatherResponse

interface WeatherRepository {

    suspend fun getInfo(weatherDataRequest: WeatherDataRequest): NetworkResult<WeatherResponse>

}