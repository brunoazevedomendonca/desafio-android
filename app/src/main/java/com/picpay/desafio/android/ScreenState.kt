package com.picpay.desafio.android

sealed class ScreenState<out T> {
    class Success<out T>(val data: T): ScreenState<T>()
    object Error : ScreenState<Nothing>()
    object Loading : ScreenState<Nothing>()
}