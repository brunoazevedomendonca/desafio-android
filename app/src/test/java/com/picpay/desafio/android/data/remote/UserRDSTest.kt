package com.picpay.desafio.android.data.remote

import com.google.common.truth.Truth.assertThat
import com.picpay.desafio.android.common.remoteModule
import com.picpay.desafio.android.data.remote.model.UserRM
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.koin.test.inject
import retrofit2.Retrofit
import java.net.HttpURLConnection

class UserRDSTest : KoinTest {

    private val retrofitBuilder: Retrofit.Builder by inject()
    private val mockWebServer = MockWebServer()
    private val testDisposables = CompositeDisposable()
    private lateinit var userRDS: UserRDS

    @Before
    fun setup() {
        startKoin { modules(remoteModule) }

        mockWebServer.start(8080)
        userRDS = retrofitBuilder
            .baseUrl(mockWebServer.url("/"))
            .build()
            .create(UserRDS::class.java)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
        testDisposables.clear()
        stopKoin()
    }

    @Test
    fun getUsers_withSuccessResponse_returnUserRMList() {
        //Given a success response
        val mockResponse = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(RemoteTestUtils.getJsonFileString("users_mock.json"))

        mockWebServer.enqueue(mockResponse)

        //When getUser is called
        userRDS
            .getUsers()
            .test()
            //Then return userRM list
            .assertComplete()
            .assertValue { it.size == 3 }
            .assertValue {
                it[0] == UserRM(
                    1001,
                    "@eduardo.santos",
                    "Eduardo Santos",
                    "https://randomuser.me/api/portraits/men/9.jpg"
                )
            }
            .addTo(testDisposables)
    }

    @Test
    fun getUsers_withEmptyList_returnEmptyList() {
        //Given a success response with empty list
        val successResponse = "[]"

        val mockResponse = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(successResponse)

        mockWebServer.enqueue(mockResponse)

        //When getUser is called
        userRDS
            .getUsers()
            .test()
            //Then return empty list
            .assertComplete()
            .assertValue(emptyList())
            .addTo(testDisposables)
    }


    @Test
    fun getUsers_sentRequest_receivedExpected() {
        //Given a success response
        val mockResponse = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)

        mockWebServer.enqueue(mockResponse)

        //When getUser is called
        userRDS.getUsers().test().addTo(testDisposables)

        //Then the correct request path is sent to server
        assertThat(mockWebServer.takeRequest().path)
            .isEqualTo("/users")
    }
}