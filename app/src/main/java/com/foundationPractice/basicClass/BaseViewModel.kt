package com.setDDG.basicClass

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

open class BaseViewModel : ViewModel() {
    private val disposable = CompositeDisposable()
    val errorMessage = MutableLiveData<String>()
    val isLoading = MutableLiveData<Boolean>()
    var page = 0
    val isRefresh = MutableLiveData<Boolean>()
    val defaultErrorMessage = "連線異常 請確認網路狀態請稍後再試 !"

    init {
        isLoading.value = false
    }

    fun <T> addDisposable(single: Single<T>, listener: DisposableSingleObserver<T>) {
        disposable.add(single.subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(listener))
    }

    override fun onCleared() {
        disposable.clear()
    }

    fun setPageZero() {
        page = 0
    }
}
