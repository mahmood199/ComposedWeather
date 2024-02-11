package com.weather.forecastify.app.ui.feature.location

import com.weather.forecastify.app.ui.state.ViewState

data class LocationViewState(
    val isLoading: Boolean = false
) : ViewState {

    companion object {
        private val locationViewState = LocationViewState()
        fun defaultState(): LocationViewState {
            return locationViewState.default()
        }
    }

    override fun default(): LocationViewState {
        return LocationViewState(
            isLoading = false
        )
    }

}