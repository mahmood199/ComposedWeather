package com.weather.forecastify.shared.ui

import com.weather.forecastify.shared.Platform
import com.weather.forecastify.shared.getPlatform

class Greeting {
    private val platform: Platform = getPlatform()

    fun greet(): String {
        return "Hello, ${platform.name}!"
    }
}