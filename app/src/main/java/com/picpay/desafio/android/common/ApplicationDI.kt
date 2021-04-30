package com.picpay.desafio.android.common

import androidx.room.Room
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.picpay.desafio.android.data.cache.UserCDS
import com.picpay.desafio.android.data.cache.dao.UserDao
import com.picpay.desafio.android.data.cache.infrastructure.AppDatabase
import com.picpay.desafio.android.data.remote.UserRDS
import com.picpay.desafio.android.data.repository.UserRepository
import com.picpay.desafio.android.presentation.scenes.userlist.UserListViewModel
import io.reactivex.disposables.CompositeDisposable
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
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

    single<UserRDS> { get<Retrofit>().create(UserRDS::class.java) }

    single<AppDatabase> {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "app-database"
        ).build()
    }

    single<UserDao> { get<AppDatabase>().userDao() }

    single<UserCDS> { UserCDS(get<UserDao>()) }

    single<UserRepository> { UserRepository(get<UserRDS>(), get<UserCDS>()) }

    single<CompositeDisposable> { CompositeDisposable() }

    viewModel { UserListViewModel(get<UserRepository>(), get<CompositeDisposable>()) }
}