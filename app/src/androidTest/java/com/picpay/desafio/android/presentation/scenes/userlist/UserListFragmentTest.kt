package com.picpay.desafio.android.presentation.scenes.userlist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.testing.launchFragment
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.picpay.desafio.android.R
import com.picpay.desafio.android.common.RxImmediateSchedulerRuleAndroid
import com.picpay.domain.model.User
import com.picpay.domain.usecase.GetUsersUC
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest

@RunWith(AndroidJUnit4::class)
class UserListFragmentTest : KoinTest {

    @get:Rule
    var activityRule: ActivityScenarioRule<FragmentActivity> =
        ActivityScenarioRule(FragmentActivity::class.java)

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var testSchedulerRule = RxImmediateSchedulerRuleAndroid()

    private val getUsersUC = mock<GetUsersUC>()
    private val compositeDisposable = CompositeDisposable()
    private val userList = listOf(User(1, "username", "name", "imageUrl"))

    private lateinit var userListViewModel: UserListViewModel

    @Before
    fun setUp() {
        whenever(getUsersUC.getObservable(GetUsersUC.GetUserParams(true)))
            .thenReturn(Observable.just(userList))
        userListViewModel = UserListViewModel(getUsersUC, compositeDisposable)

        loadKoinModules(
            module {
                viewModel(override = true) { userListViewModel }
            }
        )
    }

    @After
    fun cleanUp() {
        stopKoin()
    }

    @Test
    fun test() {
        launchFragmentInContainer { UserListFragment.newInstance() }
        onView(withId(R.id.recyclerView)).check(matches(isDisplayed()))
    }

}