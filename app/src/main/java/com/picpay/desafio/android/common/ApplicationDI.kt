package com.picpay.desafio.android.common

import androidx.room.Room
import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.picpay.desafio.android.data.cache.UserCDS
import com.picpay.desafio.android.data.cache.dao.UserDao
import com.picpay.desafio.android.data.cache.infrastructure.AppDatabase
import com.picpay.desafio.android.data.remote.UserRDS
import com.picpay.desafio.android.data.repository.UserRepository
import com.picpay.desafio.android.presentation.scenes.userlist.UserListViewModel
import com.picpay.domain.datarepository.UserDataRepository
import com.picpay.domain.di.MainScheduler
import com.picpay.domain.di.WorkerScheduler
import com.picpay.domain.usecase.GetUsersUC
import com.picpay.domain.usecase.RefreshUsersUC
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

val remoteModule = module {

    single<Gson> { GsonBuilder().create() }

    single<GsonConverterFactory> { GsonConverterFactory.create(get<Gson>()) }

    single<OkHttpClient> { OkHttpClient.Builder().build() }

    single<RxJava2CallAdapterFactory> { RxJava2CallAdapterFactory.create() }

    single<Retrofit>() {
        Retrofit.Builder()
            .baseUrl("http://careers.picpay.com/tests/mobdev/")
            .client(get<OkHttpClient>())
            .addConverterFactory(get<GsonConverterFactory>())
            .addCallAdapterFactory(get<RxJava2CallAdapterFactory>())
            .build()
    }

    single<UserRDS> { get<Retrofit>().create(UserRDS::class.java) }

}

val cacheModule = module {

    single<AppDatabase> {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "app-database"
        ).build()
    }

    single<UserDao> { get<AppDatabase>().userDao() }

    single<UserCDS> { UserCDS(get<UserDao>()) }

}

val repositoryModule = module {

    single<UserDataRepository> { UserRepository(get<UserRDS>(), get<UserCDS>()) }

}

val rxModule = module {

    factory<CompositeDisposable> { CompositeDisposable() }

    single<Scheduler>(qualifier = MainScheduler) { AndroidSchedulers.mainThread() }

    factory<Scheduler>(qualifier = WorkerScheduler) { Schedulers.io() }

}

val viewModelModule = module {

    viewModel {
        UserListViewModel(
            get<GetUsersUC>(),
            get<RefreshUsersUC>(),
            get<CompositeDisposable>()
        )
    }
}

val navigationModule = module {

    single<Cicerone<Router>> { Cicerone.create() }

    single<Router> { get<Cicerone<Router>>().router }

    single<NavigatorHolder> { get<Cicerone<Router>>().getNavigatorHolder() }
}

val useCaseModule = module {

    factory<RefreshUsersUC> {
        RefreshUsersUC(
            get<Scheduler>(qualifier = WorkerScheduler),
            get<Scheduler>(qualifier = MainScheduler),
            get<UserDataRepository>()
        )
    }

    factory<GetUsersUC> {
        GetUsersUC(
            get<Scheduler>(qualifier = WorkerScheduler),
            get<Scheduler>(qualifier = MainScheduler),
            get<UserDataRepository>()
        )
    }

}