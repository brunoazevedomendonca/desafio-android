package com.picpay.domain.usecase

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.picpay.domain.datarepository.UserDataRepository
import com.picpay.domain.model.User
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

class GetUsersUCTest {

    private val repository = mock<UserDataRepository>()
    private val testScheduler = Schedulers.trampoline()

    private val users = listOf(User(1, "username", "name", "imageUrl"))
    private val error = RuntimeException("test")

    // System under test
    private lateinit var getUsersUC: GetUsersUC

    @Before
    fun setUp() {
        getUsersUC = GetUsersUC(testScheduler, testScheduler, repository)
    }

    @Test
    fun getObservable_repositoryReturnListOfUsers_returnListOfUsers() {
        //Given repository.getUsers() returns a list of users
        whenever(repository.getUsers(any()))
            .thenReturn(Observable.just(users))

        //When getObservable is called
        val testObserver = getUsersUC
            .getObservable(GetUsersUC.GetUserParams(any()))
            .test()

        //Then return the list of users
        testObserver.assertValue(users)

        testObserver.dispose()
    }

    @Test
    fun getObservable_repositoryReturnError_returnError() {
        //Given repository.getUsers() returns an error
        whenever(repository.getUsers(any()))
            .thenReturn(Observable.error(error))

        //When getObservable is called
        val testObserver = getUsersUC
            .getObservable(GetUsersUC.GetUserParams(any()))
            .test()

        //Then returns the error
        testObserver.assertError(error)

        testObserver.dispose()
    }
}