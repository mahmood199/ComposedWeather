package com.weather.forecastify.shared

actual fun getPlatform(): Platform {
    return object : Platform {
        override val name: String
            get() = "Some platform name"
    }
}