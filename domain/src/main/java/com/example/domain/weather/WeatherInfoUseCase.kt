package com.example.domain.weather

import com.example.core_network.NetworkResult
import com.example.data.model.request.WeatherDataRequest
import com.example.data.model.response.WeatherResponse
import com.example.data.repositories.contract.WeatherRepository
import javax.inject.Inject

class WeatherInfoUseCase @Inject constructor(
    private val repository: WeatherRepository
) {

    suspend fun getInfo(weatherDataRequest: WeatherDataRequest): NetworkResult<WeatherResponse> {
        return repository.getInfo(weatherDataRequest)
    }

}