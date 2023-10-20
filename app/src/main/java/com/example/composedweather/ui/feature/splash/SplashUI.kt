package com.example.composedweather.ui.feature.splash

import android.os.Build.VERSION.SDK_INT
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import coil.size.Size
import com.example.composedweather.R
import com.example.composedweather.ui.theme.ComposedWeatherTheme
import kotlinx.coroutines.delay

private const val SplashWaitTime: Long = 5000

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

    val context = LocalContext.current

    val imageLoader = remember {
        ImageLoader.Builder(context)
            .components {
                if (SDK_INT >= 28) {
                    add(ImageDecoderDecoder.Factory())
                } else {
                    add(GifDecoder.Factory())
                }
            }
            .build()
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = rememberAsyncImagePainter(
                ImageRequest.Builder(LocalContext.current)
                    .data(data = R.drawable.weather_7108_256)
                    .apply(block = fun ImageRequest.Builder.() {
                        size(Size.ORIGINAL)
                    }).build(),
                imageLoader = imageLoader
            ),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
        )
    }
}

@Preview
@Composable
fun SplashUIPreview() {
    ComposedWeatherTheme {
        SplashUI(
            navigateToHome = {
            }
        )
    }
}