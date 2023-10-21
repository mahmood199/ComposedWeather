package com.example.connectivity

import kotlinx.coroutines.flow.Flow

interface ConnectivityObserver {

    val networkState: Flow<Boolean>

    val connected: Boolean

    val disconnected: Boolean

}
