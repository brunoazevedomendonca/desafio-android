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

    override fun refreshUsers(): Completable =
        userRDS.getUsers()
            .map { it.map { it.toCacheModel() } }
            .flatMapCompletable { userCDS.upsertUserList(it) }

    override fun getUsers(): Observable<List<User>> =
        userCDS.getUserList()
            .map { it.map { it.toDomainModel() } }
}