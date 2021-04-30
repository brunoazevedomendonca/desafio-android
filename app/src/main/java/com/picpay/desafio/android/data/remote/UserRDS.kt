package com.picpay.desafio.android.data.remote

import com.picpay.desafio.android.data.remote.model.UserRM
import io.reactivex.Single
import retrofit2.http.GET

interface UserRDS {
    @GET("users")
    fun getUsers(): Single<List<UserRM>>
}