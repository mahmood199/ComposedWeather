package com.weather.forecastify.app.ui.feature.home

import com.weather.forecastify.data.model.request.WeatherDataRequest

data class HomeViewState(
    var isLoading: Boolean = false,
    var error: String? = null,
    var isConnected: Boolean = false,
    var weatherDataRequest: WeatherDataRequest = WeatherDataRequest()
)
