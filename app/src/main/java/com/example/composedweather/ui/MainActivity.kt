package com.example.composedweather.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import com.example.composedweather.ui.theme.ComposedWeatherTheme
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val fusedLocationProviderClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            ComposedWeatherTheme {
                CentralNavigation(
                    fusedLocationProviderClient = fusedLocationProviderClient,
                    backPress = {
                        finish()
                    },
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ComposedWeatherTheme {
        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(
            LocalContext.current
        )
        CentralNavigation(
            fusedLocationProviderClient,
            {}
        )
    }
}