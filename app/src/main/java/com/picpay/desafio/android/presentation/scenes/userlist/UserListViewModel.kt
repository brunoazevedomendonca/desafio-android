package com.picpay.desafio.android.presentation.scenes.userlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.picpay.desafio.android.R
import com.picpay.desafio.android.data.cache.model.UserCM
import com.picpay.desafio.android.data.repository.UserRepository
import com.picpay.desafio.android.presentation.common.Event
import com.picpay.desafio.android.presentation.common.ScreenState
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo

class UserListViewModel(
    private val userRepository: UserRepository,
    private val compositeDisposable: CompositeDisposable
) : ViewModel() {

    private val _screenState: MutableLiveData<ScreenState<List<UserCM>>> = MutableLiveData()
    val screenState: LiveData<ScreenState<List<UserCM>>>
        get() = _screenState

    private val _refreshError: MutableLiveData<Event<Int>> = MutableLiveData()
    val refreshError: LiveData<Event<Int>>
        get() = _refreshError

    init {
        refreshUserList()

        userRepository.getUsers()
            .doOnSubscribe { _screenState.value = ScreenState.Loading }
            .subscribe(
                { _screenState.value = ScreenState.Success(it) },
                { _screenState.value = ScreenState.Error }
            ).addTo(compositeDisposable)
    }

    private fun refreshUserList() {
        userRepository
            .refreshUsers()
            .doOnError { _refreshError.value = Event(R.string.refresh_error) }
            .onErrorComplete()
            .subscribe()
            .addTo(compositeDisposable)
    }

    fun onRefresh() {
        refreshUserList()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}