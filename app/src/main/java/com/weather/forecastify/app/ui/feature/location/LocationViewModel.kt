package com.weather.forecastify.app.ui.feature.location

import androidx.lifecycle.ViewModel
import com.weather.forecastify.app.ui.state.StateManager
import kotlinx.coroutines.flow.MutableStateFlow

class LocationViewModel : ViewModel(), StateManager<LocationViewState> {

    override val _state: MutableStateFlow<LocationViewState>
        get() = MutableStateFlow(LocationViewState.defaultState())



}