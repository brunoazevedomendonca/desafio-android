package com.picpay.domain.datarepository

import com.picpay.domain.model.User
import io.reactivex.Completable
import io.reactivex.Observable

interface UserDataRepository {
    fun refreshUsers(): Completable
    fun getUsers(): Observable<List<User>>
}