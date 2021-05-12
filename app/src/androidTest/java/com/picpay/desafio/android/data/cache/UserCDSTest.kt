package com.picpay.desafio.android.data.cache

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.picpay.desafio.android.data.cache.dao.UserDao
import com.picpay.desafio.android.data.cache.infrastructure.AppDatabase
import com.picpay.desafio.android.data.cache.model.UserCM
import com.picpay.domain.exceptions.NoDataException
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UserCDSTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private val userCM1 = UserCM(1, "username1", "name1", "imageUrl1")
    private val userCM2 = UserCM(2, "username2", "name2", "imageUrl2")
    private val userCM3 = UserCM(3, "username3", "name3", "imageUrl3")

    private lateinit var appDatabase: AppDatabase
    private lateinit var userDao: UserDao
    private val testDisposables = CompositeDisposable()

    // System under test
    private lateinit var userCDS: UserCDS

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        appDatabase = Room
            .inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        userDao = appDatabase.userDao()
        userCDS = UserCDS(userDao)

    }

    @After
    fun tearDown() {
        testDisposables.clear()
        appDatabase.close()
    }

    @Test
    fun upsertUserList_newUserCMList_cacheDataReplace() {
        //Given a previous userCM list inserted in cache
        val previousUserCmList = listOf(userCM1, userCM2)
        userDao.insertAll(previousUserCmList)

        //When upsertUserList is called with a new userCM list
        val newUserCMList = listOf(userCM2, userCM3)
        userCDS.upsertUserList(newUserCMList).blockingAwait()

        //Then cache data is replaced with new data
        userDao
            .getAll()
            .test()
            .assertValue(newUserCMList)
            .addTo(testDisposables)
    }

    @Test
    fun getUserList_withCacheData_returnCacheData() {
        //Given previous cache data
        val userCMList = listOf(userCM1, userCM2)
        userDao.insertAll(userCMList).blockingAwait()

        //When getUserList is called
        userCDS
            .getUserList()
            .test()
            //Then the userCM list is returned
            .assertValue(userCMList)
            .addTo(testDisposables)
    }

    @Test
    fun getUserList_withoutCacheData_returnNoDataException() {
        //Given no data in cache

        //When getUserList is called
        userCDS
            .getUserList()
            .test()
            //Then a NoDataException is returned
            .assertError(NoDataException::class.java)
            .addTo(testDisposables)
    }
}