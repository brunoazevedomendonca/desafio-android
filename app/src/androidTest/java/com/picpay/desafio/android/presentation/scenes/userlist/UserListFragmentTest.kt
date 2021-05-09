package com.picpay.desafio.android.presentation.scenes.userlist

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nhaarman.mockitokotlin2.mock
import com.picpay.desafio.android.R
import com.picpay.desafio.android.presentation.common.MainActivity
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
    var activityRule: ActivityScenarioRule<MainActivity> =
        ActivityScenarioRule(MainActivity::class.java)

    private val userListViewModel = mock<UserListViewModel>()

    @Before
    fun setUp() {
        loadKoinModules(module { viewModel(override = true) { userListViewModel } })
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