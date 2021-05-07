package com.picpay.desafio.android.presentation.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

class LiveDataTestObserver<T>(private val liveData: LiveData<T>) : Disposable {
    private val observedValues = mutableListOf<T>()
    private val observer = Observer<T> { observedValues.add(it) }
    private var isDisposed: Boolean = false

    init {
        liveData.observeForever(observer)
    }

    fun getObservedValues(): List<T> {
        return observedValues
    }

    override fun dispose() {
        liveData.removeObserver(observer)
        isDisposed = true
    }

    override fun isDisposed(): Boolean = isDisposed

    fun addTo(compositeDisposable: CompositeDisposable) =
        apply { compositeDisposable.add(this) }
}