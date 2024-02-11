package com.weather.forecastify.app.ui.feature.location

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.weather.forecastify.app.ui.theme.ForecastifyTheme

@Composable
fun LocationUIContainer(
    modifier: Modifier = Modifier,
    viewModel: LocationViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    LocationUI(
        state = state,
        modifier = modifier.fillMaxSize()
    )
}

@Composable
fun LocationUI(
    state: LocationViewState,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {

    }
}


@Preview
@Composable
private fun LocationUIContainerPreview() {
    ForecastifyTheme {
        LocationUIContainer()
    }
}