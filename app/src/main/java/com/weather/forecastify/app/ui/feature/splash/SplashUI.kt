package com.weather.forecastify.app.ui.feature.splash

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.weather.forecastify.R
import com.weather.forecastify.app.ui.theme.ForecastifyTheme
import kotlinx.coroutines.delay

private const val SplashWaitTime: Long = 2000

@Composable
fun SplashUI(
    navigateToHome: () -> Unit,
    viewModel: SplashViewModel = hiltViewModel()
) {

    val currentOnTimeout by rememberUpdatedState(navigateToHome)

    BackHandler {}

    LaunchedEffect(Unit) {
        delay(timeMillis = SplashWaitTime)
        currentOnTimeout()
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val composition by rememberLottieComposition(
            LottieCompositionSpec.RawRes(
                resId = R.raw.splash
            )
        )

        LottieAnimation(
            composition = composition,
            iterations = LottieConstants.IterateForever,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(0.8f)
        )

    }
}

@Preview
@Composable
fun SplashUIPreview() {
    ForecastifyTheme {
        SplashUI(
            navigateToHome = {
            }
        )
    }
}