package com.example.composedweather.ui

import androidx.compose.animation.fadeIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.composedweather.ui.feature.search.DetailUI
import com.example.composedweather.ui.feature.home.HomeUI
import com.example.composedweather.ui.feature.splash.SplashUI
import com.example.composedweather.ui.theme.ComposedWeatherTheme
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

@Composable
fun CentralNavigation(
    fusedLocationProviderClient: FusedLocationProviderClient,
    backPress: () -> Unit,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.name,
        modifier = modifier
    ) {
        composable(route = Screen.Splash.name) {
            SplashUI(
                navigateToHome = {
                    navController.navigate(route = Screen.Home.name) {
                        popUpTo(Screen.Splash.name) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(
            route = Screen.Home.name,
            enterTransition = {
                fadeIn()
            }
        ) {
            HomeUI(
                fusedLocationProviderClient = fusedLocationProviderClient,
                onBackPressed = {
                    backPress()
                },
                navigateToSearch = {
                    navController.navigate(Screen.Detail.name)
                }
            )
        }

        composable(
            route = Screen.Detail.name,
            enterTransition = {
                fadeIn()
            }
        ) {
            DetailUI(onBackPressed = {
                navController.popBackStack()
            })
        }


    }

}

@Preview
@Composable
fun CentralNavigationPreview() {
    ComposedWeatherTheme {
        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(
            LocalContext.current
        )
        CentralNavigation(
            fusedLocationProviderClient = fusedLocationProviderClient,
            backPress = {}
        )
    }
}