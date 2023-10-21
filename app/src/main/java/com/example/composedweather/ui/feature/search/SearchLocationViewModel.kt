package com.example.composedweather.ui.feature.search

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.connectivity.NetworkConnectivityObserver
import com.example.core_network.NetworkResult
import com.example.data.model.response.LocationResponseItem
import com.example.domain.location.LocationUseCase
import com.example.domain.user.UserPreferencesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchLocationViewModel @Inject constructor(
    private val networkConnectivityObserver: NetworkConnectivityObserver,
    private val locationUseCase: LocationUseCase,
    private val userPreferencesUseCase: UserPreferencesUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(SearchLocationViewState())
    val state = _state.asStateFlow()

    private val _query = MutableStateFlow("")
    val uiQuery = _query.asStateFlow()

    var searchResults = mutableStateListOf<LocationResponseItem>()

    init {
        viewModelScope.launch {
            networkConnectivityObserver.networkState.collectLatest {
                _state.value = _state.value.copy(isConnected = it)
            }
        }
    }

    fun updateSearchQuery(it: String) {
        _query.value = it
        if (it.isEmpty()) {
            searchResults.clear()
        }
        _state.value = _state.value.copy(isInSearchMode = false)
    }

    fun searchLocation() {
        viewModelScope.launch(Dispatchers.IO) {
            _state.value = _state.value.copy(isLoading = true)
            when (val result = locationUseCase.searchByName(_query.value)) {
                is NetworkResult.Exception -> handleError(result.e)
                is NetworkResult.RedirectError -> handleError(result.e)
                is NetworkResult.ServerError -> handleError(result.e)
                is NetworkResult.UnAuthorised -> handleError(result.e)

                is NetworkResult.Success -> {
                    searchResults.clear()
                    searchResults.addAll(result.data)
                }
            }
            _state.value = _state.value.copy(isLoading = false, isInSearchMode = true)
        }
    }

    private fun handleError(e: Throwable) {
        _state.value = _state.value.copy(error = e.message)
        _state.value = _state.value.copy(isLoading = false)
    }

    fun setLocationCoordinates(locationResponseItem: LocationResponseItem) {
        viewModelScope.launch(Dispatchers.IO) {
            userPreferencesUseCase.setUserLocation(
                latitude = locationResponseItem.lat.toDoubleOrNull() ?: 0.0,
                longitude = locationResponseItem.lon.toDoubleOrNull() ?: 0.0,
                location = locationResponseItem.displayName,
                isLocationDetected = false
            )
        }
    }

    fun resetError() {
        _state.value = _state.value.copy(error = null)
    }

}