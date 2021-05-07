package com.picpay.desafio.android.presentation.scenes.userlist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.picpay.desafio.android.presentation.common.ScreenState
import com.picpay.desafio.android.presentation.common.getOrAwaitValue
import com.picpay.desafio.android.presentation.common.observeForTesting
import com.picpay.domain.model.User
import com.picpay.domain.usecase.GetUsersUC
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.TestScheduler
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.TimeUnit

class UserListViewModelTest {

    private val getUsersUC = mock<GetUsersUC>()
    private val compositeDisposable = CompositeDisposable()

    private val userList = listOf(User(1, "username", "name", "imageUrl"))
    private val error = RuntimeException("test")

    //System under test
    private lateinit var userListViewModel: UserListViewModel

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @Test
    fun initViewModel_getUsersUCSuccess_updateLiveData() {
        //Given GetUsersUC return with success
        val testScheduler = TestScheduler()
        whenever(getUsersUC.getObservable(any()))
            .thenReturn(Observable.just(userList).subscribeOn(testScheduler))

        //When viewModel is initialized
        userListViewModel = UserListViewModel(getUsersUC, compositeDisposable)

        val states = mutableListOf<ScreenState>()
        userListViewModel.screenState.observeForTesting {
            states.add(it)
            testScheduler.triggerActions()
        }
        val isRefreshing = userListViewModel.isRefreshing.getOrAwaitValue()
        val users = userListViewModel.users.getOrAwaitValue()
        val message = userListViewModel.message.getOrAwaitValue(500, TimeUnit.MILLISECONDS, false)

        //Then loading and success are published in screenState
        // isRefreshing is set to false
        // users is updated
        // message is not updated
        assertEquals(2, states.size)
        assertEquals(ScreenState.LOADING, states[0])
        assertEquals(ScreenState.SUCCESS, states[1])
        assertEquals(false, isRefreshing)
        assertEquals(userList, users)
        assertEquals(null, message)
    }

    @Test
    fun initViewModel_getUsersUCError_updateLiveData() {
        //Given GetUsersUC return error
        val testScheduler = TestScheduler()
        whenever(getUsersUC.getObservable(any()))
            .thenReturn(Observable.error<List<User>>(error).subscribeOn(testScheduler))

        //When viewModel is initialized
        userListViewModel = UserListViewModel(getUsersUC, compositeDisposable)

        val states = mutableListOf<ScreenState>()
        userListViewModel.screenState.observeForTesting {
            states.add(it)
            testScheduler.triggerActions()
        }
        val isRefreshing = userListViewModel.isRefreshing.getOrAwaitValue()
        val message = userListViewModel.message.getOrAwaitValue()
        val users = userListViewModel.users.getOrAwaitValue(500, TimeUnit.MILLISECONDS, false)

        //Then loading and error are published in screenState
        // isRefreshing is set to false
        // message is updated
        // users is not updated
        assertEquals(2, states.size)
        assertEquals(ScreenState.LOADING, states[0])
        assertEquals(ScreenState.ERROR, states[1])
        assertEquals(false, isRefreshing)
        assertTrue(message != null)
        assertEquals(null, users)
    }

    //TODO validar quantidade de vezes que livedatas são notificados

    //TODO validar métodos onRefresh e onTryAgain e onCleared


}