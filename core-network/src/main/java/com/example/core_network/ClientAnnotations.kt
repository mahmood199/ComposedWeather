package com.example.core_network

import javax.inject.Qualifier


// Annotation in case multiple clients are required.

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class WeatherClient

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class LocationClient

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class TertiaryClient

