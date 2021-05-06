package com.picpay.desafio.android.presentation.scenes.userlist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.picpay.desafio.android.presentation.common.ScreenState
import com.picpay.domain.model.User
import com.picpay.domain.usecase.GetUsersUC
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.TestScheduler
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class UserListViewModelTest {

    private val testScheduler = TestScheduler()
    private val getUsersUC = mock<GetUsersUC>()
    private val compositeDisposable = CompositeDisposable()

    private val userList = listOf(User(1, "username", "name", "imageUrl"))

    //System under test
    private lateinit var userListViewModel: UserListViewModel

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
//        userListViewModel = UserListViewModel(getUsersUC, refreshUsersUC, compositeDisposable)
    }

    @Test
    fun getUsers_userListReturned_updateScreenState(){
        //Given GetUsersUC return with success
        whenever(getUsersUC.getObservable(mock()))
            .thenReturn(Observable.just(userList).subscribeOn(testScheduler))

        //When viewModel is started
        userListViewModel = UserListViewModel(getUsersUC, compositeDisposable)

        //Then loading and success are published in screenState
        val states = mutableListOf<ScreenState>()
        userListViewModel.screenState.observeForever {
            states.add(it)
            testScheduler.triggerActions()
        }

        assertEquals(2, states.size)
        assertEquals(ScreenState.LOADING, states[0])
        assertEquals(ScreenState.SUCCESS, states[1])
    }

}