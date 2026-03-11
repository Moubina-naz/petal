package com.example.petal

import android.app.Application
import android.content.pm.PackageManager
import com.example.petal.data.remote.ApiProvider
import com.google.android.libraries.places.api.Places

class PetalApp : Application() {
    override fun onCreate() {
        super.onCreate()
        ApiProvider.initialize(this)


        val apiKey = BuildConfig.MAPS_API_KEY
        if (apiKey.isNotEmpty() && !Places.isInitialized()) {
            Places.initialize(this, apiKey)
        }
        android.util.Log.d("PLACES_INIT", "Is initialized: ${Places.isInitialized()}")
    }
    }
