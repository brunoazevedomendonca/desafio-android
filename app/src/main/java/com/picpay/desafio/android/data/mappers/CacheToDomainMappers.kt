package com.picpay.desafio.android.data.mappers

import com.picpay.desafio.android.data.cache.model.UserCM
import com.picpay.domain.model.User

fun UserCM.toDomainModel(): User =
    User(username, name, img)