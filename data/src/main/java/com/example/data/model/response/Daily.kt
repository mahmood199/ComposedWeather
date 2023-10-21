package com.example.data.model.response

import com.google.gson.annotations.SerializedName

data class Daily(
    @SerializedName("precipitation_sum") val precipitationSum: List<Double>,
    @SerializedName("precipitation_hours") val precipitationHours: List<Int>,
    @SerializedName("temperature_2m_max") val temperature2mMax: List<Double>,
    @SerializedName("temperature_2m_min") val temperature2mMin: List<Double>,
    @SerializedName("time") val time: List<String>
)
