package com.weather.forecastify.app.ui.feature.home

import com.weather.forecastify.app.ui.state.ViewState
import com.weather.forecastify.data.model.request.WeatherDataRequest

data class HomeViewState(
    var isLoading: Boolean = false,
    var error: String? = null,
    var isConnected: Boolean = false,
    var weatherDataRequest: WeatherDataRequest = WeatherDataRequest()
) : ViewState {

    companion object {
        private val homeViewState = HomeViewState()
        fun defaultState(): HomeViewState {
            return homeViewState.default()
        }
    }

    override fun default(): HomeViewState {
        return HomeViewState(
            isLoading = false,
            error = null,
            isConnected = false,
            weatherDataRequest = WeatherDataRequest()
        )
    }
}
