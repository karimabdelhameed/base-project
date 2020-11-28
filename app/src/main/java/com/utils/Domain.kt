package com.utils


object Domain {

    lateinit var application: App private set


    fun integrateWith(application: App) {
        Domain.application = application
    }
}