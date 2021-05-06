package com.picpay.desafio.android.data.repository

import com.picpay.desafio.android.data.cache.UserCDS
import com.picpay.desafio.android.data.mappers.toCacheModel
import com.picpay.desafio.android.data.mappers.toDomainModel
import com.picpay.desafio.android.data.remote.UserRDS
import com.picpay.domain.datarepository.UserDataRepository
import com.picpay.domain.model.User
import io.reactivex.Completable
import io.reactivex.Observable

class UserRepository(
    private val userRDS: UserRDS,
    private val userCDS: UserCDS
) : UserDataRepository {

    override fun getUsers(forceToRefresh: Boolean): Observable<List<User>> =
        refreshUsersIfRequested(forceToRefresh)
            .andThen(userCDS.getUserList())
            .map { it.map { it.toDomainModel() } }

    private fun refreshUsersIfRequested(forceToRefresh: Boolean): Completable =
        if (forceToRefresh) {
            userRDS.getUsers()
                .map { it.map { it.toCacheModel() } }
                .flatMapCompletable { userCDS.upsertUserList(it) }
                .onErrorComplete()
        } else Completable.complete()

}