package com.picpay.desafio.android.data.cache

import com.picpay.desafio.android.data.cache.dao.UserDao
import com.picpay.desafio.android.data.cache.model.UserCM
import com.picpay.domain.exceptions.NoDataException
import io.reactivex.Completable
import io.reactivex.Observable

class UserCDS(private val userDao: UserDao) {

    fun upsertUserList(userList: List<UserCM>): Completable =
        userDao
            .deleteAll()
            .andThen(userDao.insertAll(userList))

    fun getUserList(): Observable<List<UserCM>> =
        userDao.getAll()
            .flatMap {
                if (it.isEmpty()) Observable.error(NoDataException())
                else Observable.just(it)
            }

}