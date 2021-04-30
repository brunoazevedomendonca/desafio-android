package com.picpay.desafio.android.data.repository

import com.picpay.desafio.android.data.cache.UserCDS
import com.picpay.desafio.android.data.cache.model.UserCM
import com.picpay.desafio.android.data.mappers.toCacheModel
import com.picpay.desafio.android.data.remote.UserRDS
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class UserRepository(
    private val userRDS: UserRDS,
    private val userCDS: UserCDS
) {

    fun refreshUsers() : Completable =
        userRDS.getUsers()
            .map { it.map { it.toCacheModel() } }
            .flatMapCompletable { userCDS.upsertUserList(it) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun getUsers(): Observable<List<UserCM>> =
        userCDS.getUserList()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
}