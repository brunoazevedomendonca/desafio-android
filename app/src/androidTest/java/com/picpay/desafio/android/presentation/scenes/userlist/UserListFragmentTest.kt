package com.picpay.desafio.android.presentation.scenes.userlist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.picpay.desafio.android.R
import com.picpay.desafio.android.common.RxImmediateSchedulerAndroidRule
import com.picpay.domain.model.User
import com.picpay.domain.usecase.GetUsersUC
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import org.hamcrest.CoreMatchers.not
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
    var testSchedulerRule = RxImmediateSchedulerAndroidRule()

    private val userListSubject = PublishSubject.create<List<User>>()
    private val userList = listOf(User(1, "username", "name", "imageUrl"))
    private val error = RuntimeException("test")

    // System under test
    private lateinit var userListFragmentScenario: FragmentScenario<UserListFragment>

    @Before
    fun setUp() {
        val getUsersUC = mock<GetUsersUC>()
        whenever(getUsersUC.getObservable(GetUsersUC.GetUserParams(true)))
            .thenReturn(userListSubject)

        loadKoinModules(
            module {
                viewModel { UserListViewModel(getUsersUC, CompositeDisposable()) }
            }
        )

        userListFragmentScenario = launchFragmentInContainer { UserListFragment.newInstance() }
    }

    @After
    fun cleanUp() {
        stopKoin()
    }

    @Test
    fun onViewCreated_getUsersWithSuccess_showSuccessState() {
        //Given a UserListFragment initialized
        //When an userList is posted in users live data
        userListSubject.onNext(userList)

        //Then show recyclerView, hide progressBar, textViewError and tryAgain
        onView(withId(R.id.recyclerViewUsers)).check(matches(isDisplayed()))
        onView(withId(R.id.progressIndicator)).check(matches(not(isDisplayed())))
        onView(withId(R.id.textViewError)).check(matches(not(isDisplayed())))
        onView(withId(R.id.btnTryAgain)).check(matches(not(isDisplayed())))
    }

    @Test
    fun onViewCreated_gettingData_showLoadingState() {
        //Given a UserListFragment initialized
        //When data is updating
        //Then show progressBar, hide recyclerView, textViewError and tryAgain
        onView(withId(R.id.progressIndicator)).check(matches(isDisplayed()))
        onView(withId(R.id.recyclerViewUsers)).check(matches(not(isDisplayed())))
        onView(withId(R.id.textViewError)).check(matches(not(isDisplayed())))
        onView(withId(R.id.btnTryAgain)).check(matches(not(isDisplayed())))
    }

    @Test
    fun onViewCreated_getUsersError_showErrorState() {
        //Given a UserListFragment initialized
        //When an error is posted in users live data
        userListSubject.onError(error)

        //Then show textViewError and tryAgain, hide progressBar and recyclerView,
        onView(withId(R.id.textViewError)).check(matches(isDisplayed()))
        onView(withId(R.id.btnTryAgain)).check(matches(isDisplayed()))
        onView(withId(R.id.recyclerViewUsers)).check(matches(not(isDisplayed())))
        onView(withId(R.id.progressIndicator)).check(matches(not(isDisplayed())))

        //TODO crash with toast
        // RuntimeException: Can't toast on a thread that has not called Looper.prepare()
    }

}