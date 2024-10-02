package com.weather.forecastify.app.util

import android.annotation.SuppressLint
import java.text.SimpleDateFormat

@SuppressLint("SimpleDateFormat")
fun String.formatToDMMMY(): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd")
    val outputFormat = SimpleDateFormat("EEEE, d MMM")

    return try {
        val date = inputFormat.parse(this)
        outputFormat.format(date)
    } catch (e: Exception) {
        e.printStackTrace()
        "Error"
    }
}

@SuppressLint("SimpleDateFormat")
fun String.formatToAMPM(): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm")
    val outputFormat = SimpleDateFormat("ha")

    return try {
        val date = inputFormat.parse(this)
        return outputFormat.format(date)
    } catch (e: Exception) {
        e.printStackTrace()
        "Error"
    }
}