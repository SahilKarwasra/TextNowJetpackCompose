package com.example.textnowjetpackcompose

import android.app.Application
import com.example.textnowjetpackcompose.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class TextNowApp: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin{
            androidContext(this@TextNowApp)
            modules(appModule)
        }
    }
}