package com.picpay.domain.usecase

import com.nhaarman.mockitokotlin2.mock
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

    // System under test
    private lateinit var getUsersUC: GetUsersUC

    @Before
    fun setUp() {
        getUsersUC = GetUsersUC(testScheduler, testScheduler, repository)
    }

    @Test
    fun getObservable_repositoryReturnListOfUsers_returnListOfUsers() {
        //Given repository.getUsers() returns a list of users
        val userList =
            listOf(
                User(1, "@a", "A", "aUrl"),
                User(2, "@b", "B", "bUrl")
            )

        Mockito.`when`(repository.getUsers())
            .thenReturn(Observable.just(userList))

        //When getObservable is called
        val testObserver = getUsersUC.getObservable(Unit).test()

        //Then return the list of users without error
        testObserver
            .assertComplete()
            .assertNoErrors()
            .assertValueCount(1)
            .assertValue(userList)

        testObserver.dispose()
    }

    @Test
    fun getObservable_repositoryReturnError_returnError() {
        //Given repository.getUsers() returns an error
        val error = Exception()

        Mockito.`when`(repository.getUsers())
            .thenReturn(Observable.error(error))

        //When getObservable is called
        val testObserver = getUsersUC.getObservable(Unit).test()

        //Then returns the error
        testObserver
            .assertError(error)
            .assertNoValues()
            .assertNotComplete()

        testObserver.dispose()
    }
}