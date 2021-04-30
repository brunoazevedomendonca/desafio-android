package com.picpay.desafio.android.data.cache.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.picpay.desafio.android.data.cache.model.UserCM
import io.reactivex.Completable
import io.reactivex.Observable

@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    fun getAll(): Observable<List<UserCM>>

    @Insert
    fun insertAll(userList: List<UserCM>): Completable

    @Query("DELETE FROM user")
    fun deleteAll(): Completable
}