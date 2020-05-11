package com.reschikov.gxbdevelopment.testtask

import android.app.Application
import com.reschikov.gxbdevelopment.testtask.di.appModule
import com.reschikov.gxbdevelopment.testtask.di.netModule
import com.reschikov.gxbdevelopment.testtask.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class TestTaskApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext (this@TestTaskApp)
            modules(listOf(appModule, viewModelModule, netModule ))
        }
    }
}