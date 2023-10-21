package com.example.data.model.response

data class DailyForecast(
    val precipitationSum: Double,
    val precipitationSumUnit: String,
    val precipitationHours: Int,
    val precipitationHoursUnit: String,
    val temperature2mMax: Double,
    val temperature2mMaxUnit: String,
    val temperature2mMin: Double,
    val temperature2mMinUnit: String,
    val time: String,
    val timeUnit: String
)