package com.picpay.desafio.android.common

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner

class DesafioAndroidApplicationTest : AndroidJUnitRunner() {
    override fun newApplication(
        cl: ClassLoader?,
        className: String?,
        context: Context?
    ): Application {
        return super.newApplication(cl, DesafioAndroidApplication::class.java.name, context)
    }
}