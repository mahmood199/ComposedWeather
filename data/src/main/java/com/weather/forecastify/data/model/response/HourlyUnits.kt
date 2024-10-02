package com.weather.forecastify.data.model.response

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class HourlyUnits(
    @SerializedName("relativehumidity_2m") val relativeHumidity2m: String,
    @SerializedName("temperature_2m") val temperature2m: String,
    @SerializedName("time") val time: String
)