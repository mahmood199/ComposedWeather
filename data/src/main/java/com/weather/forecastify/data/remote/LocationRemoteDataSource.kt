package com.weather.forecastify.data.remote

import com.weather.forecastify.data.model.response.LocationResponseItem
import com.weather.forecastify.core_network.NetworkResult
import com.weather.forecastify.core_network.ResponseProcessor
import com.google.gson.Gson
import com.weather.forecastify.data.BuildConfig
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import java.io.IOException
import javax.inject.Inject

class LocationRemoteDataSource @Inject constructor(
    private val client: HttpClient,
    private val responseProcessor: ResponseProcessor,
    private val gson: Gson
) {

    suspend fun getLocations(location: String): NetworkResult<List<LocationResponseItem>> {
        return try {
            val response = client.get(BuildConfig.NOMINATIM_BASE_URL + "search") {
                parameter("q", location)
                parameter("format", "jsonv2")
                parameter("limit", 10)
            }

            val result = responseProcessor.getResultFromListResponse<LocationResponseItem>(gson, response)
            result
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is IOException) {
                NetworkResult.Exception(Throwable("Please your internet connection"))
            } else {
                NetworkResult.Exception(Throwable("Something went wrong"))
            }
        }
    }


}