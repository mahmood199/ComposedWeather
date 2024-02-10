package com.weather.forecastify.data.model.response

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class DailyUnits(
    @SerializedName("precipitation_sum") val precipitationSum: String,
    @SerializedName("precipitation_hours") val precipitationHours: String,
    @SerializedName("temperature_2m_max") val temperature2mMax: String,
    @SerializedName("temperature_2m_min") val temperature2mMin: String,
    @SerializedName("time") val time: String
)