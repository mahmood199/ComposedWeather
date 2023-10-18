package com.example.composedweather.ui.feature.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composedweather.core.NetworkResult
import com.example.composedweather.data.repo.contract.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: WeatherRepository
) : ViewModel() {

    private val _state = MutableStateFlow(DetailViewState())
    val state = _state.asStateFlow()


    init {
        getInfo()
    }

    private fun getInfo() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.getInfo()
            when (result) {
                is NetworkResult.Exception -> {}
                is NetworkResult.RedirectError -> {}
                is NetworkResult.ServerError -> {}
                is NetworkResult.Success -> {

                }
                is NetworkResult.UnAuthorised -> {}
            }
        }
    }


}