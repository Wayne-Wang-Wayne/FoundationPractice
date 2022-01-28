package com.foundationPractice.yTPlayerFunction

import android.app.Application
import androidx.annotation.NonNull
import androidx.lifecycle.MutableLiveData
import com.foundationPractice.videomanager.VideoModel
import com.foundationPractice.api.APIService
import com.foundationPractice.basicClass.BaseAndroidViewModel
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