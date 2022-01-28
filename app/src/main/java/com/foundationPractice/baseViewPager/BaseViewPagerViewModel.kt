package com.foundationPractice.baseViewPager

import android.app.Application
import androidx.annotation.NonNull
import androidx.lifecycle.MutableLiveData
import com.foundationPractice.api.APIService
import com.foundationPractice.basicClass.BaseAndroidViewModel
import io.reactivex.observers.DisposableSingleObserver

 class BaseViewPagerViewModel(@NonNull application: Application) : BaseAndroidViewModel(application) {
    private val TAG: String = javaClass.simpleName
    val newsTabs = MutableLiveData<List<NewsTabsModel>>()

    fun fetchNewsTabs(){
        isLoading.value = true
        addDisposable(APIService.getNewsTabs(getApplication()),object :DisposableSingleObserver<List<NewsTabsModel>>(){
            override fun onSuccess(t: List<NewsTabsModel>) {
                newsTabs.value = t
                isLoading.value = false
            }
            override fun onError(e: Throwable) {
                errorMessage.value = defaultErrorMessage
                isLoading.value = false
            }



        })
    }
}