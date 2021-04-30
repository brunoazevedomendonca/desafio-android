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

    private val _screenState: MutableLiveData<ScreenState<List<User>>> = MutableLiveData()
    val screenState: LiveData<ScreenState<List<User>>>
        get() = _screenState

    private val _refreshError: MutableLiveData<Event<Int>> = MutableLiveData()
    val refreshError: LiveData<Event<Int>>
        get() = _refreshError

    init {
        refreshUserList()

        getUsersUC.getObservable(Unit)
            .doOnSubscribe { _screenState.value = ScreenState.Loading }
            .subscribe(
                { _screenState.value = ScreenState.Success(it) },
                { _screenState.value = ScreenState.Error }
            ).addTo(compositeDisposable)
    }

    private fun refreshUserList() {
        refreshUsersUC.getCompletable(Unit)
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