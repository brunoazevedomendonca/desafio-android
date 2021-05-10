package com.picpay.desafio.android.presentation.scenes.userlist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.picpay.desafio.android.common.RxImmediateSchedulerRule
import com.picpay.desafio.android.presentation.common.LiveDataTestObserver
import com.picpay.desafio.android.presentation.common.ScreenState
import com.picpay.domain.model.User
import com.picpay.domain.usecase.GetUsersUC
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class UserListViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var testSchedulerRule = RxImmediateSchedulerRule()

    private val getUsersUC = mock<GetUsersUC>()
    private val subject = PublishSubject.create<List<User>>()
    private val userList = listOf(User(1, "username", "name", "imageUrl"))
    private val error = RuntimeException("test")
    private val testDisposables = CompositeDisposable()

    //System under test
    private lateinit var userListViewModel: UserListViewModel

    @Before
    fun setUp() {
        whenever(getUsersUC.getObservable(any()))
            .thenReturn(subject)
    }

    @After
    fun tearDown() {
        testDisposables.clear()
    }

    @Test
    fun initViewModel_getUsersUCSuccess_updateLiveData() {
        //Given UserListViewModel init
        userListViewModel = UserListViewModel(getUsersUC, CompositeDisposable())

        val stateObserver = LiveDataTestObserver(userListViewModel.screenState).addTo(testDisposables)
        val isRefreshingObserver = LiveDataTestObserver(userListViewModel.isRefreshing).addTo(testDisposables)
        val usersObserver = LiveDataTestObserver(userListViewModel.users).addTo(testDisposables)
        val messageObserver = LiveDataTestObserver(userListViewModel.message).addTo(testDisposables)

        //When GetUserUC return with success
        subject.onNext(userList)

        //Then loading and success are published in screenState
        // isRefreshing is set to false
        // users is updated
        // message is not updated
        assertThat(stateObserver.getObservedValues())
            .containsExactly(ScreenState.LOADING, ScreenState.SUCCESS)
            .inOrder()

        assertThat(isRefreshingObserver.getObservedValues())
            .containsExactly(false)

        assertThat(usersObserver.getObservedValues())
            .containsExactly(userList)

        assertThat(messageObserver.getObservedValues())
            .isEmpty()
    }

    @Test
    fun initViewModel_getUsersUCError_updateLiveData() {
        //Given UserListViewModel init
        userListViewModel = UserListViewModel(getUsersUC, CompositeDisposable())

        val stateObserver = LiveDataTestObserver(userListViewModel.screenState).addTo(testDisposables)
        val isRefreshingObserver = LiveDataTestObserver(userListViewModel.isRefreshing).addTo(testDisposables)
        val usersObserver = LiveDataTestObserver(userListViewModel.users).addTo(testDisposables)
        val messageObserver = LiveDataTestObserver(userListViewModel.message).addTo(testDisposables)

        //When GetUserUC return error
        subject.onError(error)

        //Then loading and error are published in screenState
        // isRefreshing is set to false
        // users is not updated
        // message is updated
        assertThat(stateObserver.getObservedValues())
            .containsExactly(ScreenState.LOADING, ScreenState.ERROR)
            .inOrder()

        assertThat(isRefreshingObserver.getObservedValues())
            .containsExactly(false)

        assertThat(usersObserver.getObservedValues())
            .isEmpty()

        assertThat(messageObserver.getObservedValues())
            .hasSize(1)
    }

    @Test
    fun onRefresh_getUsersUCSuccess_updateLiveData() {
        //Given UserListViewModel previously initialized with data
        userListViewModel = UserListViewModel(getUsersUC, CompositeDisposable())

        val stateObserver = LiveDataTestObserver(userListViewModel.screenState).addTo(testDisposables)
        val isRefreshingObserver = LiveDataTestObserver(userListViewModel.isRefreshing).addTo(testDisposables)
        val usersObserver = LiveDataTestObserver(userListViewModel.users).addTo(testDisposables)
        val messageObserver = LiveDataTestObserver(userListViewModel.message).addTo(testDisposables)

        subject.onNext(userList)

        //When onRefresh is called
        userListViewModel.onRefresh()
        subject.onNext(userList)

        //Then loading and success are published twice in screenState
        // isRefreshing is set to false twice
        // users is updated twice
        // message is not updated
        assertThat(stateObserver.getObservedValues())
            .containsExactly(ScreenState.LOADING, ScreenState.SUCCESS, ScreenState.LOADING, ScreenState.SUCCESS)
            .inOrder()

        assertThat(isRefreshingObserver.getObservedValues())
            .containsExactly(false, false)

        assertThat(usersObserver.getObservedValues())
            .containsExactly(userList, userList)

        assertThat(messageObserver.getObservedValues())
            .isEmpty()
    }

    @Test
    fun onTryAgain_getUsersUCSuccess_updateLiveData() {
        //Given the UserListViewModel previously initialized with error
        userListViewModel = UserListViewModel(getUsersUC, CompositeDisposable())

        val stateObserver = LiveDataTestObserver(userListViewModel.screenState).addTo(testDisposables)
        val isRefreshingObserver = LiveDataTestObserver(userListViewModel.isRefreshing).addTo(testDisposables)
        val usersObserver = LiveDataTestObserver(userListViewModel.users).addTo(testDisposables)
        val messageObserver = LiveDataTestObserver(userListViewModel.message).addTo(testDisposables)

        subject.onError(error)

        //When onTryAgain is called and return with success
        whenever(getUsersUC.getObservable(any()))
            .thenReturn(Observable.just(userList))
        userListViewModel.onTryAgain()

        //Then loading, error, loading and success are published in screenState
        // isRefreshing is set to false twice
        // message is updated
        // users is updated
        assertThat(stateObserver.getObservedValues())
            .containsExactly(ScreenState.LOADING, ScreenState.ERROR, ScreenState.LOADING, ScreenState.SUCCESS)
            .inOrder()

        assertThat(isRefreshingObserver.getObservedValues())
            .containsExactly(false, false)

        assertThat(messageObserver.getObservedValues())
            .hasSize(1)

        assertThat(usersObserver.getObservedValues())
            .containsExactly(userList)
    }
}