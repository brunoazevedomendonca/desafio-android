package com.picpay.desafio.android

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(
    service: PicPayService
) : ViewModel() {

    private val _screenState: MutableLiveData<ScreenState<List<User>>> = MutableLiveData()
    val screenState: LiveData<ScreenState<List<User>>>
        get() = _screenState

    init {
        _screenState.value = ScreenState.Loading

        service.getUsers()
            .enqueue(object : Callback<List<User>> {
                override fun onFailure(call: Call<List<User>>, t: Throwable) {
                    _screenState.value = ScreenState.Error
                }

                override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                    if (response.isSuccessful && response.body() != null) {
                        _screenState.value = ScreenState.Success(response.body()!!)
                    } else {
                        _screenState.value = ScreenState.Error
                    }

                }
            })
    }

}