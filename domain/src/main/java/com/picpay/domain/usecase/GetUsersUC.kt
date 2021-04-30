package com.picpay.domain.usecase

import com.picpay.domain.datarepository.UserDataRepository
import com.picpay.domain.model.User
import io.reactivex.Observable
import io.reactivex.Scheduler

class GetUsersUC(
    executorScheduler: Scheduler,
    postExecutionScheduler: Scheduler,
    private val userDataRepository: UserDataRepository
) : ObservableUseCase<List<User>, Unit>(executorScheduler, postExecutionScheduler) {

    override fun getRawObservable(params: Unit): Observable<List<User>> =
        userDataRepository.getUsers()
}