package com.picpay.desafio.android.presentation.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

class LiveDataTestObserver<T>(private val liveData: LiveData<T>) {
    private val observedValues = mutableListOf<T>()
    private val observer = Observer<T> { observedValues.add(it) }

    init {
        liveData.observeForever(observer)
    }

    fun getObservedValues(): List<T> {
        dispose()
        return observedValues
    }

    fun dispose() {
        liveData.removeObserver(observer)
    }
}