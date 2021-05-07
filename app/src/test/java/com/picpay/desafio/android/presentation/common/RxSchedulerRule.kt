package com.picpay.desafio.android.presentation.common

import io.reactivex.Scheduler
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

class RxSchedulerRule(private val defaultScheduler: Scheduler = Schedulers.trampoline()) : TestRule {
    override fun apply(base: Statement, description: Description?): Statement {
        return object : Statement() {
            @Throws(Throwable::class)
            override fun evaluate() {
                RxJavaPlugins.setIoSchedulerHandler { defaultScheduler }
                RxJavaPlugins.setComputationSchedulerHandler { defaultScheduler }
                RxJavaPlugins.setNewThreadSchedulerHandler { defaultScheduler }
                RxAndroidPlugins.setInitMainThreadSchedulerHandler { defaultScheduler }
                try {
                    base.evaluate()
                } finally {
                    RxJavaPlugins.reset()
                    RxAndroidPlugins.reset()
                }
            }
        }
    }
}