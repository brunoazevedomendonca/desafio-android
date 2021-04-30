package com.picpay.domain.usecase

import com.picpay.domain.datarepository.UserDataRepository
import io.reactivex.Completable
import io.reactivex.Scheduler

class RefreshUsersUC(
    executorScheduler: Scheduler,
    postExecutionScheduler: Scheduler,
    private val userDataRepository: UserDataRepository
) : CompletableUseCase<Unit>(executorScheduler, postExecutionScheduler) {

    override fun getRawCompletable(params: Unit): Completable =
        userDataRepository.refreshUsers()
}