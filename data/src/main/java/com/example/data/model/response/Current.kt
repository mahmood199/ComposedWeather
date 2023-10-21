package com.example.data.model.response

import com.google.gson.annotations.SerializedName

data class Current(
    @SerializedName("apparent_temperature") val apparent_temperature: Double,
    @SerializedName("interval") val interval: Int,
    @SerializedName("is_day") val is_day: Int,
    @SerializedName("precipitation") val precipitation: Int,
    @SerializedName("rain") val rain: Int,
    @SerializedName("relativehumidity_2m") val relativehumidity_2m: Int,
    @SerializedName("showers") val showers: Int,
    @SerializedName("temperature_2m") val temperature_2m: Double,
    @SerializedName("time") val time: String,
    @SerializedName("winddirection_10m") val winddirection_10m: Int,
    @SerializedName("windspeed_10m") val windspeed_10m: Double
)