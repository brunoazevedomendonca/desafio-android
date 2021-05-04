package com.picpay.desafio.android.data.remote

import com.google.gson.GsonBuilder
import com.picpay.desafio.android.data.remote.model.UserRM
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.net.HttpURLConnection

class UserRDSTest {

    companion object {
        private const val ID = 1
        private const val USERNAME = "username"
        private const val NAME = "name"
        private const val IMAGE_URL = "imageUrl"
    }

    private val mockWebServer = MockWebServer()
    private lateinit var userRDS: UserRDS

    @Before
    fun setup() {
        mockWebServer.start(8080)
        userRDS = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .client(OkHttpClient.Builder().build())
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(UserRDS::class.java)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
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