package com.picpay.desafio.android.data.cache.dao

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.picpay.desafio.android.data.cache.infrastructure.AppDatabase
import com.picpay.desafio.android.data.cache.model.UserCM
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class UserDaoTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private val userCMList = listOf(UserCM(1, "username", "name", "imageUrl"))

    private lateinit var appDatabase: AppDatabase
    private val testDisposables = CompositeDisposable()

    //System under test
    private lateinit var userDao: UserDao

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        appDatabase = Room
            .inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        userDao = appDatabase.userDao()
    }

    @After
    fun tearDown() {
        testDisposables.clear()
        appDatabase.close()
    }

    @Test
    fun getAll_withoutCacheData_returnEmptyList() {
        //Given no data in cache
        //When getAll is called
        userDao
            .getAll()
            .test()
            //Then an empty list is returned
            .assertValue(emptyList())
            .addTo(testDisposables)
    }

    @Test
    fun getAll_withCacheData_returnCacheData() {
        //Given a previous userCM list inserted in cache
        userDao.insertAll(userCMList).blockingAwait()

        //When getAll is called
        userDao
            .getAll()
            .test()
            //Then the cache data is returned
            .assertValue(userCMList)
            .addTo(testDisposables)
    }

    @Test
    fun insertAll_emptyList_emptyListInserted() {
        //Given an empty list
        //When insertAll is called
        userDao.insertAll(emptyList()).blockingAwait()

        //Then the emptyList is inserted
        userDao
            .getAll()
            .test()
            .assertValue(emptyList())
            .addTo(testDisposables)
    }

    @Test
    fun insertAll_userCMList_userCMListInserted() {
        //Given a userCMList
        //When insertAll is called
        userDao.insertAll(userCMList).blockingAwait()

        //Then the userCMList is inserted
        userDao
            .getAll()
            .test()
            .assertValue(userCMList)
            .addTo(testDisposables)
    }

    @Test
    fun deleteAll_withCacheData_deleteAllData() {
        //Given previous cache data
        userDao.insertAll(userCMList)

        //When delete all is called
        userDao.deleteAll().blockingAwait()

        //Then all cache data is deleted
        userDao
            .getAll()
            .test()
            .assertValue(emptyList())
            .addTo(testDisposables)
    }

}