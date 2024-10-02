package com.weather.forecastify.domain.weather

import com.weather.forecastify.core_network.NetworkResult
import com.weather.forecastify.data.model.request.WeatherDataRequest
import com.weather.forecastify.data.model.response.WeatherResponse
import com.weather.forecastify.data.repositories.contract.WeatherRepository
import javax.inject.Inject

class WeatherInfoUseCase @Inject constructor(
    private val repository: WeatherRepository
) {

    suspend fun getInfo(weatherDataRequest: WeatherDataRequest): NetworkResult<WeatherResponse> {
        return repository.getInfo(weatherDataRequest)
    }

}