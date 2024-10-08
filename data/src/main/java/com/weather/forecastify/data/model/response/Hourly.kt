package com.weather.forecastify.data.model.response

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class Hourly(
    @SerializedName("relativehumidity_2m") val relativeHumidity2m: List<Int>,
    @SerializedName("temperature_2m") val temperature2m: List<Double>,
    @SerializedName("time") val time: List<String>,
)