package com.picpay.desafio.android.data.remote

import com.picpay.desafio.android.data.remote.model.User
import io.reactivex.Single
import retrofit2.http.GET

interface PicPayRDS {

    @GET("users")
    fun getUsers(): Single<List<User>>
}