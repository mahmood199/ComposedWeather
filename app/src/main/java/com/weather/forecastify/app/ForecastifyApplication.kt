package com.weather.forecastify.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

@HiltAndroidApp
class ForecastifyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@ForecastifyApplication)
            androidLogger()
        }
    }

}

fun performApiCall(): Result<Int> {
    return try {
        val response = 4
        Result.success(response)
    } catch (e: Exception) {
        Result.failure(e)
    }
}