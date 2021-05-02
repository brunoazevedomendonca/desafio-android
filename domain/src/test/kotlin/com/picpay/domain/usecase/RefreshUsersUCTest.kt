package com.picpay.domain.usecase

import com.nhaarman.mockitokotlin2.mock
import com.picpay.domain.datarepository.UserDataRepository
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

class RefreshUsersUCTest {

    private val repository = mock<UserDataRepository>()
    private val testScheduler = Schedulers.trampoline()

    // System under test
    private lateinit var refreshUsersUC: RefreshUsersUC

    @Before
    fun setUp() {
        refreshUsersUC = RefreshUsersUC(testScheduler, testScheduler, repository)
    }

    @Test
    fun getCompletable_repositoryCompletes_completeWithoutError() {
        //Given repository.refreshUsers() completes
        Mockito.`when`(repository.refreshUsers())
            .thenReturn(Completable.complete())

        //When getCompletable is called
        val testObserver = refreshUsersUC.getCompletable(Unit).test()

        //Then completes without error
        testObserver
            .assertComplete()
            .assertNoErrors()
            .assertNoValues()

        testObserver.dispose()
    }

    @Test
    fun getCompletable_repositoryReturnError_returnError() {
        //Given repository.refreshUsers() returns an error
        val error = Exception()

        Mockito.`when`(repository.refreshUsers())
            .thenReturn(Completable.error(error))

        //When getCompletable is called
        val testObserver = refreshUsersUC.getCompletable(Unit).test()

        //Then returns the error
        testObserver
            .assertError(error)
            .assertNoValues()
            .assertNotComplete()

        testObserver.dispose()
    }
}