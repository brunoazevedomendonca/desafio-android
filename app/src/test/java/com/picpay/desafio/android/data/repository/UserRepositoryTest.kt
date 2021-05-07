package com.picpay.desafio.android.data.repository

import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.picpay.desafio.android.data.cache.UserCDS
import com.picpay.desafio.android.data.cache.model.UserCM
import com.picpay.desafio.android.data.remote.UserRDS
import com.picpay.desafio.android.data.remote.model.UserRM
import com.picpay.domain.model.User
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import org.junit.After
import org.junit.Before
import org.junit.Test

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
    private val testDisposables = CompositeDisposable()

    // System under test
    private lateinit var userRepository: UserRepository

    @Before
    fun setUp() {
        userRepository = UserRepository(userRDS, userCDS)
    }

    @After
    fun tearDown() {
        testDisposables.clear()
    }

    @Test
    fun getUsers_forceToRefreshAndRemoteSuccess_userCDSReceiveListAndReturnFromCache() {
        //Given userRDS.getUsers() returns a list of userRM
        whenever(userRDS.getUsers())
            .thenReturn(Single.just(remoteUsers))

        val userCMCaptor = argumentCaptor<List<UserCM>>()
        whenever(userCDS.upsertUserList(userCMCaptor.capture()))
            .thenReturn(Completable.complete())

        whenever(userCDS.getUserList())
            .thenReturn(Observable.just(cacheUsers))

        //When getUsers is called with forceToRefresh = true
        userRepository
            .getUsers(true)
            .test()
            //Then the mapped data is passed to userCDS.upsertUserList() and returns cache data in domain model
            .assertValue(domainUsers)
            .addTo(testDisposables)

        assertThat(userCMCaptor.firstValue).isEqualTo(cacheUsers)
    }

    @Test
    fun getUsers_forceToRefreshAndRemoteError_userCDSReceiveNothingAndReturnFromCache() {
        //Given userRDS.getUsers() returns an error
        whenever(userRDS.getUsers())
            .thenReturn(Single.error(error))

        val userCMCaptor = argumentCaptor<List<UserCM>>()
        whenever(userCDS.upsertUserList(userCMCaptor.capture()))
            .thenReturn(Completable.complete())

        whenever(userCDS.getUserList())
            .thenReturn(Observable.just(cacheUsers))

        //When getUsers is called with forceToRefresh = true
        userRepository
            .getUsers(true)
            .test()
            //Then nothing is passed to userCDS.upsertUserList() and returns cache data in domain model
            .assertValue(domainUsers)
            .addTo(testDisposables)

        assertThat(userCMCaptor.allValues).isEmpty()
    }

    @Test
    fun getUsers_forceToRefreshAndCacheError_userCDSReceiveListAndReturnError() {
        //Given userRDS.getUsers() returns a list of userRM
        whenever(userRDS.getUsers())
            .thenReturn(Single.just(remoteUsers))

        val userCMCaptor = argumentCaptor<List<UserCM>>()
        whenever(userCDS.upsertUserList(userCMCaptor.capture()))
            .thenReturn(Completable.complete())

        whenever(userCDS.getUserList())
            .thenReturn(Observable.error(error))

        //When getUsers is called with forceToRefresh = true
        userRepository
            .getUsers(true)
            .test()
            //Then the mapped data is passed to userCDS.upsertUserList() and returns the error
            .assertError(error)
            .addTo(testDisposables)

        assertThat(userCMCaptor.firstValue).isEqualTo(cacheUsers)
    }

    @Test
    fun getUsers_fromCacheWithSuccess_userCDSReceiveNothingAndReturnFromCache() {
        //Given a previous cached data
        whenever(userCDS.getUserList())
            .thenReturn(Observable.just(cacheUsers))

        val userCMCaptor = argumentCaptor<List<UserCM>>()
        whenever(userCDS.upsertUserList(userCMCaptor.capture()))
            .thenReturn(Completable.complete())

        //When getUsers is called with forceToRefresh = false
        userRepository
            .getUsers(false)
            .test()
            //Then nothing is passed to userCDS.upsertUserList() and returns cache data in domain model
            .assertValue(domainUsers)
            .addTo(testDisposables)

        assertThat(userCMCaptor.allValues).isEmpty()
    }

    @Test
    fun getUsers_fromCacheWithError_userCDSReceiveNothingAndReturnError() {
        //Given an error in userCDS.getUserList()
        whenever(userCDS.getUserList())
            .thenReturn(Observable.error(error))

        val userCMCaptor = argumentCaptor<List<UserCM>>()
        whenever(userCDS.upsertUserList(userCMCaptor.capture()))
            .thenReturn(Completable.complete())

        //When getUsers is called with forceToRefresh = false
        userRepository
            .getUsers(false)
            .test()
            //Then nothing is passed to userCDS.upsertUserList() and returns the error
            .assertError(error)
            .addTo(testDisposables)

        assertThat(userCMCaptor.allValues).isEmpty()
    }
}