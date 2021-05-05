package com.picpay.desafio.android.data.cache

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.picpay.desafio.android.data.cache.dao.UserDao
import com.picpay.desafio.android.data.cache.infrastructure.AppDatabase
import com.picpay.desafio.android.data.cache.model.UserCM
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
        val testObserver = userDao.getAll().test()
        testObserver.assertValue(newUserCMList)

        testObserver.dispose()
    }

    @Test
    fun getUserList_withCacheData_returnCacheData() {
        //Given previous cache data
        val userCMList = listOf(userCM1, userCM2)
        userDao.insertAll(userCMList).blockingAwait()

        //When getUserList is called
        val testObserver = userDao.getAll().test()

        //Then the userCM list is returned
        testObserver.assertValue(userCMList)

        testObserver.dispose()
    }

    @Test
    fun getUserList_withoutCacheData_returnEmtpyList() {
        //Given no data in cache

        //When getUserList is called
        val testObserver = userDao.getAll().test()

        //Then an empty list is returned
        testObserver.assertValue(emptyList())

        testObserver.dispose()
    }
}