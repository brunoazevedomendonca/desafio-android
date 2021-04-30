package com.picpay.desafio.android.data.cache.infrastructure

import androidx.room.Database
import androidx.room.RoomDatabase
import com.picpay.desafio.android.data.cache.dao.UserDao
import com.picpay.desafio.android.data.cache.model.UserCM

@Database(entities = [UserCM::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}