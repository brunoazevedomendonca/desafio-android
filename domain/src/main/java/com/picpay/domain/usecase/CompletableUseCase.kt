package com.picpay.domain.usecase

import io.reactivex.Completable
import io.reactivex.Scheduler

abstract class CompletableUseCase<in Request>(
    private val executorScheduler: Scheduler,
    private val postExecutionScheduler: Scheduler
) {

    internal abstract fun getRawCompletable(params: Request): Completable

    fun getCompletable(request: Request): Completable =
        getRawCompletable(request)
            .subscribeOn(executorScheduler)
            .observeOn(postExecutionScheduler)
}