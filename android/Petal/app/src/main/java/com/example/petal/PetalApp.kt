package com.example.petal

import android.app.Application
import com.example.petal.data.remote.ApiProvider

class PetalApp : Application() {
    override fun onCreate() {
        super.onCreate()
        ApiProvider.initialize(this)
    }
}