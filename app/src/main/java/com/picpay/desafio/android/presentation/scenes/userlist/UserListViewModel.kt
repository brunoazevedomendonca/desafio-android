package com.picpay.desafio.android.presentation.scenes.userlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.picpay.desafio.android.data.remote.model.User
import com.picpay.desafio.android.data.repository.UserRepository
import com.picpay.desafio.android.presentation.scenes.common.ScreenState
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo

class UserListViewModel(
    userRepository: UserRepository,
    private val compositeDisposable: CompositeDisposable
) : ViewModel() {

    private val _screenState: MutableLiveData<ScreenState<List<User>>> = MutableLiveData()
    val screenState: LiveData<ScreenState<List<User>>>
        get() = _screenState

    init {
        _screenState.value = ScreenState.Loading

        userRepository.getUsers()
            .subscribe(
                { _screenState.value = ScreenState.Success(it) },
                { _screenState.value = ScreenState.Error }
            ).addTo(compositeDisposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

}