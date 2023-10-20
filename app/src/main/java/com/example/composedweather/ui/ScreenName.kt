package com.example.composedweather.ui


object ScreenName {
    const val HOME = "home"
    const val DETAIL = "detail"
    const val SPLASH = "splash"
}


sealed class Screen(val name: String) {
    data object Home : Screen(name = ScreenName.HOME)
    data object Detail : Screen(name = ScreenName.DETAIL)
    data object Splash : Screen(name = ScreenName.SPLASH)
}