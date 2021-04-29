package com.picpay.desafio.android

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {

    single<Gson> { GsonBuilder().create() }

    single<GsonConverterFactory> { GsonConverterFactory.create(get<Gson>()) }

    single<OkHttpClient> { OkHttpClient.Builder().build() }

    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl("http://careers.picpay.com/tests/mobdev/")
            .client(get<OkHttpClient>())
            .addConverterFactory(get<GsonConverterFactory>())
            .build()
    }

    single<PicPayService> { get<Retrofit>().create(PicPayService::class.java) }

    viewModel { MainViewModel(get<PicPayService>()) }
}