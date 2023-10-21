package com.example.composedweather.ui.feature.home

import com.example.data.model.request.WeatherDataRequest

data class HomeViewState(
    var isLoading: Boolean = false,
    var error: String? = null,
    var isConnected: Boolean = false,
    var weatherDataRequest: WeatherDataRequest = WeatherDataRequest()
)
