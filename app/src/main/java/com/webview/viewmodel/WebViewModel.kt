package com.set.app.entertainment.webview.viewmodel

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import com.set.app.entertainment.basicClass.BaseViewModel

class WebViewModel() : BaseViewModel() {

    val data = MutableLiveData<Bundle>()

    init {

    }

    fun setWebViewActivity(bundle: Bundle) {
        data.value = bundle
    }

    override fun onCleared() {
        super.onCleared()

    }

}
