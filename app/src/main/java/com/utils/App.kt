package com.utils

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Created by karim on 28,November,2020
 */
@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()
    }
}