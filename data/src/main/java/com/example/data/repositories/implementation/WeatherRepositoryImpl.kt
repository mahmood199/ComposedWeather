package com.example.data.repositories.implementation

import com.example.core_network.NetworkResult
import com.example.data.model.request.WeatherDataRequest
import com.example.data.model.response.WeatherResponse
import com.example.data.remote.WeatherRemoteDataSource
import com.example.data.repositories.contract.WeatherRepository
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val remoteDataSource: WeatherRemoteDataSource
): WeatherRepository {

    override suspend fun getInfo(weatherDataRequest: WeatherDataRequest): NetworkResult<WeatherResponse> {
        return remoteDataSource.getWeatherForecast(weatherDataRequest)
    }

}