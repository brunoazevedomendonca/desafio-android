package com.picpay.desafio.android.data.repository

import com.picpay.desafio.android.data.remote.PicPayRDS
import com.picpay.desafio.android.data.remote.model.User
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class UserRepository(
    private val picPayRDS: PicPayRDS
) {

    fun getUsers(): Single<List<User>> =
        picPayRDS.getUsers()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
}