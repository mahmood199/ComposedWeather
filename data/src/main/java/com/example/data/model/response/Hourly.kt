package com.example.data.model.response

import com.google.gson.annotations.SerializedName

data class Hourly(
    @SerializedName("relativehumidity_2m") val relativeHumidity2m: List<Int>,
    @SerializedName("temperature_2m") val temperature2m: List<Double>,
    @SerializedName("time") val time: List<String>,
)