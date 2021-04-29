package com.picpay.desafio.android.common

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.picpay.desafio.android.data.remote.PicPayRDS
import com.picpay.desafio.android.presentation.scenes.userlist.UserListViewModel
import io.reactivex.disposables.CompositeDisposable
import okhttp3.OkHttpClient
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {

    single<Gson> { GsonBuilder().create() }

    single<GsonConverterFactory> { GsonConverterFactory.create(get<Gson>()) }

    single<OkHttpClient> { OkHttpClient.Builder().build() }

    single<RxJava2CallAdapterFactory> { RxJava2CallAdapterFactory.create() }

    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl("http://careers.picpay.com/tests/mobdev/")
            .client(get<OkHttpClient>())
            .addConverterFactory(get<GsonConverterFactory>())
            .addCallAdapterFactory(get<RxJava2CallAdapterFactory>())
            .build()
    }

    single<PicPayRDS> { get<Retrofit>().create(PicPayRDS::class.java) }

    single<CompositeDisposable> { CompositeDisposable() }

    viewModel { UserListViewModel(get<PicPayRDS>(), get<CompositeDisposable>()) }
}