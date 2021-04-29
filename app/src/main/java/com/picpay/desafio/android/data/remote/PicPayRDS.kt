package com.picpay.desafio.android.data.remote

import com.picpay.desafio.android.data.remote.model.User
import retrofit2.Call
import retrofit2.http.GET

interface PicPayRDS {

    @GET("users")
    fun getUsers(): Call<List<User>>
}