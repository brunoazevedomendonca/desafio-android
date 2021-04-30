package com.picpay.desafio.android.data.mappers

import com.picpay.desafio.android.data.cache.model.UserCM
import com.picpay.desafio.android.data.remote.model.UserRM

fun UserRM.toCacheModel(): UserCM =
    UserCM(id, img, name, username)