package com.weather.forecastify.shared

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform