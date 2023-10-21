package com.example.data.remote

import com.example.data.BuildConfig
import com.example.data.model.request.Constants
import com.example.data.model.request.WeatherDataRequest
import com.example.data.model.response.WeatherResponse
import com.example.core_network.NetworkResult
import com.example.core_network.ResponseProcessor
import com.google.gson.Gson
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import java.io.IOException
import javax.inject.Inject

class WeatherRemoteDataSource @Inject constructor(
    private val httpClient: HttpClient,
    private val responseProcessor: ResponseProcessor,
    private val gson: Gson
) {

    suspend fun getWeatherForecast(request: WeatherDataRequest): NetworkResult<WeatherResponse> {
        return try {
            val response = httpClient.get(BuildConfig.OPEN_METEO_BASE_URL + "forecast") {
                parameter("latitude", request.latitude)
                parameter("longitude", request.longitude)
                parameter("forecast_days", request.forecastDays)
                if (request.currentDayRequested) {
                    val parsedRequest = request.params.toString().replace(Regex("[\\[\\] ]"), "")
                    parameter("current", parsedRequest)
                }
                parameter(Constants.TEMPERATURE_UNIT, request.temperatureUnit)
                if (request.isHourlyDataRequested) {
                    parameter("hourly", "temperature_2m")
                    parameter("hourly", "relativehumidity_2m")
                    parameter("hourly", "dewpoint_2m")
                }

                parameter(
                    key = Constants.DAILY,
                    value = listOf(
                        Constants.TEMPERATURE_2M_MAX,
                        Constants.TEMPERATURE_2M_MIN,
                        Constants.PRECIPITATION_SUM,
                        Constants.PRECIPITATION_HOURS,
                    ).toString().replace(Regex("[\\[\\] ]"), "")
                )
                parameter(Constants.TIME_ZONE, "Asia/Singapore")
            }
            val result = responseProcessor.getResultFromResponse<WeatherResponse>(gson, response)
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