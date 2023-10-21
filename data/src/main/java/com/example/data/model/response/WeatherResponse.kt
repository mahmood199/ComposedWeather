package com.example.data.model.response

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    @SerializedName("latitude") val latitude: Double,
    @SerializedName("longitude") val longitude: Double,
    @SerializedName("generationtime_ms") val generationTimeMs: Double,
    @SerializedName("utc_offset_seconds") val utcOffsetSeconds: Int,
    @SerializedName("timezone") val timezone: String,
    @SerializedName("timezone_abbreviation") val timezoneAbbreviation: String,
    @SerializedName("elevation") val elevation: Int,

    @SerializedName("hourly") val hourly: Hourly,
    @SerializedName("hourly_units") val hourlyUnits: HourlyUnits,
    @SerializedName("current") val current: Current,
    @SerializedName("current_units") val currentUnits: CurrentUnits,
    @SerializedName("daily") val daily: Daily,
    @SerializedName("daily_units") val dailyUnits: DailyUnits,
)