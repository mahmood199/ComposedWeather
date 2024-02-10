package com.weather.forecastify.connectivity_android

import kotlinx.coroutines.flow.Flow

interface ConnectivityObserver {

    val networkState: Flow<Boolean>

    val connected: Boolean

    val disconnected: Boolean

}
