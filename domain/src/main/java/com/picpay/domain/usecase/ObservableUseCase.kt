package com.picpay.domain.usecase

import io.reactivex.Observable
import io.reactivex.Scheduler

abstract class ObservableUseCase<Response, in Request>(
    private val executorScheduler: Scheduler,
    private val postExecutionScheduler: Scheduler
) {

    internal abstract fun getRawObservable(params: Request): Observable<Response>

    fun getObservable(request: Request): Observable<Response> =
        getRawObservable(request)
            .subscribeOn(executorScheduler)
            .observeOn(postExecutionScheduler)
}