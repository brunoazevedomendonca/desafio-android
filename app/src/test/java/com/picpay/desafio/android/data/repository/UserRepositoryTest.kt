package com.picpay.desafio.android.data.repository

import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.mock
import com.picpay.desafio.android.data.cache.UserCDS
import com.picpay.desafio.android.data.cache.model.UserCM
import com.picpay.desafio.android.data.remote.UserRDS
import com.picpay.desafio.android.data.remote.model.UserRM
import com.picpay.domain.model.User
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

class UserRepositoryTest {

    companion object {
        private const val ID = 1
        private const val USERNAME = "username"
        private const val NAME = "name"
        private const val IMAGE_URL = "imageUrl"
    }

    private val userRDS = mock<UserRDS>()
    private val userCDS = mock<UserCDS>()
    private val remoteUsers = listOf(UserRM(ID, USERNAME, NAME, IMAGE_URL))
    private val cacheUsers = listOf(UserCM(ID, USERNAME, NAME, IMAGE_URL))
    private val domainUsers = listOf(User(ID, USERNAME, NAME, IMAGE_URL))
    private val error = RuntimeException("test")

    private lateinit var userRepository: UserRepository

    @Before
    fun setUp() {
        userRepository = UserRepository(userRDS, userCDS)
    }

    @Test
    fun refreshUser_userRDSReturnUserRMList_userCDSReceiveUserCMList() {
        //Given userRDS.getUsers() returns a list of userRM
        Mockito.`when`(userRDS.getUsers())
            .thenReturn(Single.just(remoteUsers))

        val userCMCaptor = argumentCaptor<List<UserCM>>()
        Mockito.`when`(userCDS.upsertUserList(userCMCaptor.capture()))
            .thenReturn(Completable.complete())

        //When refreshUser is called
        val testObserver = userRepository.refreshUsers().test()

        //Then the mapped data is passed to userCDS.upsertUserList() and completes without error
        assertEquals(userCMCaptor.firstValue, cacheUsers)

        testObserver.assertComplete()

        testObserver.dispose()
    }

    @Test
    fun refreshUser_userRDSReturnError_userCDSReceiveNothingAndReturnError() {
        //Given userRDS returns an error
        Mockito.`when`(userRDS.getUsers())
            .thenReturn(Single.error(error))

        val userCMCaptor = argumentCaptor<List<UserCM>>()
        Mockito.`when`(userCDS.upsertUserList(userCMCaptor.capture()))
            .thenReturn(Completable.complete())

        //When refreshUser is called
        val testObserver = userRepository.refreshUsers().test()

        //Then nothing is passed to userCDS.upsertUserList() and returns the error
        assertTrue(userCMCaptor.allValues.isEmpty())

        testObserver.assertError(error)

        testObserver.dispose()
    }

    @Test
    fun refreshUser_userCDSReturnError_returnError() {
        //Given userRDS.getUsers() returns a list of userRM and userCDS.upsertUserList return an error
        Mockito.`when`(userRDS.getUsers())
            .thenReturn(Single.just(remoteUsers))

        val userCMCaptor = argumentCaptor<List<UserCM>>()
        Mockito.`when`(userCDS.upsertUserList(userCMCaptor.capture()))
            .thenReturn(Completable.error(error))

        //When refreshUser is called
        val testObserver = userRepository.refreshUsers().test()

        //Then mapped data is passed to userCDS.upsertUserList() and returns the error
        assertEquals(userCMCaptor.firstValue, cacheUsers)

        testObserver.assertError(error)

        testObserver.dispose()
    }

    @Test
    fun getUsers_userCDSReturnListOfUserCM_returnListOfUser() {
        //Given userCDS.getUsers() return a list os userCM

        Mockito.`when`(userCDS.getUserList())
            .thenReturn(Observable.just(cacheUsers))

        //When getUsers is called
        val testObserver = userRepository.getUsers().test()

        //Then return a list of User
        testObserver
            .assertComplete()
            .assertNoErrors()
            .assertValueCount(1)
            .assertValue(domainUsers)

        testObserver.dispose()
    }

    @Test
    fun getUsers_userCDSReturnError_returnError() {
        //Given userCDS.getUsers() return an error
        Mockito.`when`(userCDS.getUserList())
            .thenReturn(Observable.error(error))

        //When getUsers is called
        val testObserver = userRepository.getUsers().test()

        //Then returns the error
        testObserver.assertError(error)

        testObserver.dispose()
    }
}