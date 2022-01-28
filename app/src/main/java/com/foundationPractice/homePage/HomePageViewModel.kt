package com.setDDG.homePage

import android.app.Application
import androidx.annotation.NonNull
import androidx.lifecycle.MutableLiveData
import com.setDDG.api.APIService
import com.setDDG.basicClass.BaseAndroidViewModel
import io.reactivex.observers.DisposableSingleObserver


class HomePageViewModel(@NonNull application: Application) : BaseAndroidViewModel(application) {
    private val TAG: String = javaClass.simpleName

    val bottomBarData = MutableLiveData<ArrayList<BottomBarModel>>()
    fun fetchBottomBar() {
        addDisposable(APIService.getBottomBar(getApplication()),
            object : DisposableSingleObserver<ArrayList<BottomBarModel>>() {
                override fun onSuccess(t: ArrayList<BottomBarModel>) {
                    bottomBarData.value = t
                }

                override fun onError(e: Throwable) {

                }

            })
    }

}