package com.weather.forecastify.data.repositories.implementation

import com.weather.forecastify.core_network.NetworkResult
import com.weather.forecastify.data.model.request.WeatherDataRequest
import com.weather.forecastify.data.model.response.WeatherResponse
import com.weather.forecastify.data.remote.WeatherRemoteDataSource
import com.weather.forecastify.data.repositories.contract.WeatherRepository
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val remoteDataSource: WeatherRemoteDataSource
) : WeatherRepository {

    override suspend fun getInfo(weatherDataRequest: WeatherDataRequest): NetworkResult<WeatherResponse> {
        return remoteDataSource.getWeatherForecast(weatherDataRequest)
    }

}