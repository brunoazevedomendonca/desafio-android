package com.picpay.desafio.android.common

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.module.Module

class DesafioAndroidApplicationTest : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@DesafioAndroidApplicationTest)
            emptyList<Module>()
        }
    }
}