package com.picpay.desafio.android.data.remote

import com.picpay.desafio.android.common.remoteModule
import com.picpay.desafio.android.data.remote.model.UserRM
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

    companion object {
        private const val ID = 1
        private const val USERNAME = "username"
        private const val NAME = "name"
        private const val IMAGE_URL = "imageUrl"
    }

    private val retrofitBuilder: Retrofit.Builder by inject()
    private val mockWebServer = MockWebServer()
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
        stopKoin()
    }

    @Test
    fun getUsers_withSuccessResponse_returnUserRMList() {
        //Given a success response
        val successResponse =
            "[{\"id\":$ID,\"name\":\"$NAME\",\"img\":\"$IMAGE_URL\",\"username\":\"$USERNAME\"}]"

        val mockResponse = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(successResponse)

        mockWebServer.enqueue(mockResponse)

        //When getUser is called
        val testObserver = userRDS.getUsers().test()

        //Then return userRM list
        testObserver
            .assertValue(listOf(UserRM(ID, USERNAME, NAME, IMAGE_URL)))
            .assertComplete()

        testObserver.dispose()
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
        val testObserver = userRDS.getUsers().test()

        //Then return empty list
        testObserver
            .assertComplete()
            .assertValue(emptyList())
    }

}