package com.picpay.desafio.android.presentation.scenes.userlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.picpay.desafio.android.R
import com.picpay.desafio.android.presentation.common.Event
import com.picpay.desafio.android.presentation.common.ScreenState
import com.picpay.domain.model.User
import com.picpay.domain.usecase.GetUsersUC
import com.picpay.domain.usecase.RefreshUsersUC
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo

class UserListViewModel(
    getUsersUC: GetUsersUC,
    private val refreshUsersUC: RefreshUsersUC,
    private val compositeDisposable: CompositeDisposable
) : ViewModel() {

    private val _users: MutableLiveData<List<User>> = MutableLiveData()
    val users: LiveData<List<User>>
        get() = _users

    private val _screenState: MutableLiveData<ScreenState> = MutableLiveData()
    val screenState: LiveData<ScreenState>
        get() = _screenState

    private val _isRefreshing: MutableLiveData<Boolean> = MutableLiveData()
    val isRefreshing: LiveData<Boolean>
        get() = _isRefreshing

    private val _message: MutableLiveData<Event<Int>> = MutableLiveData()
    val message: LiveData<Event<Int>>
        get() = _message

    init {
        refreshUserList()

        getUsersUC.getObservable(Unit)
            .doOnSubscribe { _screenState.value = ScreenState.LOADING }
            .doOnError { _screenState.value = ScreenState.ERROR }
            .doOnNext { _screenState.value = ScreenState.SUCCESS }
            .subscribe(
                { _users.value = it },
                { _message.value = Event(R.string.error) }
            )
            .addTo(compositeDisposable)
    }

    private fun refreshUserList() {
        refreshUsersUC.getCompletable(Unit)
            .doOnError { _message.value = Event(R.string.refresh_error) }
            .doOnComplete { _isRefreshing.value = false }
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