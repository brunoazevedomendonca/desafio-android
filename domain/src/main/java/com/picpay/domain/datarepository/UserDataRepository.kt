package com.picpay.domain.datarepository

import com.picpay.domain.model.User
import io.reactivex.Observable

interface UserDataRepository {
    fun getUsers(forceToRefresh: Boolean): Observable<List<User>>
}