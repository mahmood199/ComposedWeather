package com.example.composedweather.data.repository.implementation

import com.example.core_network.NetworkResult
import com.example.composedweather.data.models.request.WeatherDataRequest
import com.example.composedweather.data.models.response.WeatherResponse
import com.example.composedweather.data.remote.WeatherRemoteDataSource
import com.example.composedweather.data.repository.contract.WeatherRepository
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val remoteDataSource: WeatherRemoteDataSource
): WeatherRepository {

    override suspend fun getInfo(weatherDataRequest: WeatherDataRequest): NetworkResult<WeatherResponse> {
        return remoteDataSource.getWeatherForecast(weatherDataRequest)
    }

}