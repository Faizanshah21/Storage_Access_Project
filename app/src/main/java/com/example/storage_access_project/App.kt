package com.example.storage_access_project

import android.app.Application
import android.content.Context

class App : Application() {

    companion object{
        lateinit var appInstance: App
        fun getAppContext(): Context = appInstance.applicationContext
    }

    override fun onCreate() {
        appInstance = this
        super.onCreate()
    }

}