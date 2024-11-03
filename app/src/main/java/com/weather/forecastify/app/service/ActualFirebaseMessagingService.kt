package com.weather.forecastify.app.service

import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ActualFirebaseMessagingService: LifeCycleAwareFirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        lifecycleScope.launch {
            sendTokenToBackend()
        }
    }

    private suspend fun sendTokenToBackend() {
        delay(2000)
    }
}