package com.picpay.desafio.android

import com.picpay.desafio.android.data.remote.PicPayRDS
import com.picpay.desafio.android.data.remote.model.User

class ExampleService(
    private val picPayRDS: PicPayRDS
) {

    fun example(): List<User> {
        val users = picPayRDS.getUsers().execute()

        return users.body() ?: emptyList()
    }
}