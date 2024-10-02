package com.weather.forecastify.app.ui.state

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

interface StateManager<T : ViewState> {

    val _state: MutableStateFlow<T>
    val state
        get() = _state.asStateFlow()

    val stateLock: Mutex
        get() = Mutex()

    suspend fun updateState(updater: T.() -> T) {
        stateLock.withLock {
            _state.value = _state.value.updater()
        }
    }
}