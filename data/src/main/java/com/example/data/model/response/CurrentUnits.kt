package com.example.data.model.response

import com.google.gson.annotations.SerializedName

data class CurrentUnits(
    @SerializedName("apparent_temperature") val apparent_temperature: String,
    @SerializedName("interval") val interval: String,
    @SerializedName("is_day") val is_day: String,
    @SerializedName("precipitation") val precipitation: String,
    @SerializedName("rain") val rain: String,
    @SerializedName("relativehumidity_2m") val relativehumidity_2m: String,
    @SerializedName("showers") val showers: String,
    @SerializedName("temperature_2m") val temperature_2m: String,
    @SerializedName("time") val time: String,
    @SerializedName("winddirection_10m") val winddirection_10m: String,
    @SerializedName("windspeed_10m") val windspeed_10m: String
)