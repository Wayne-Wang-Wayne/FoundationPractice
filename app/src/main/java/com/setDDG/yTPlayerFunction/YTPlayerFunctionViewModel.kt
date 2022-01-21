package com.setDDG.yTPlayerFunction

import android.app.Application
import androidx.annotation.NonNull
import androidx.lifecycle.MutableLiveData
import com.setDDG.videomanager.VideoModel
import com.setDDG.api.APIService
import com.setDDG.basicClass.BaseAndroidViewModel
import io.reactivex.observers.DisposableSingleObserver

class YTPlayerFunctionViewModel(@NonNull application: Application) : BaseAndroidViewModel(application) {

    val videoListData = MutableLiveData<VideoModel>()

    fun fetchVideo(){
        addDisposable(APIService.getVideoInfo(getApplication()),object :DisposableSingleObserver<VideoModel>(){
            override fun onSuccess(t: VideoModel) {
                videoListData.value = t
                isLoading.value = false
            }

            override fun onError(e: Throwable) {
                isLoading.value = false
            }
        })
    }
}