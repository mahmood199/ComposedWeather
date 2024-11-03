package com.weather.forecastify.app.service

import android.annotation.SuppressLint
import android.content.Intent
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.google.firebase.messaging.FirebaseMessagingService

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
open class LifeCycleAwareFirebaseMessagingService: FirebaseMessagingService(), LifecycleOwner {

    private val dispatcher =  ServiceLifecycleDispatcher(this)

    override val lifecycle: Lifecycle
        get() = dispatcher.lifecycle


    override fun onCreate() {
        dispatcher.onServicePreSuperOnCreate()
        super.onCreate()
    }

    override fun onRebind(intent: Intent?) {
        dispatcher.onServicePreSuperOnBind()
        super.onRebind(intent)
    }

    override fun onDestroy() {
        dispatcher.onServicePreSuperOnDestroy()
        super.onDestroy()
    }

}