package com.picpay.desafio.android.presentation.scenes.common

class Event<T>(private val content: T) {

    private var hasBeenHandled: Boolean = false

    fun executeIfNotHandled(executeFunction: (T) -> Unit) {
        if (!hasBeenHandled) {
            executeFunction(content)
            hasBeenHandled = true
        }
    }

    fun execute(executeFunction: (T) -> Unit) {
        executeFunction(content)
    }

}