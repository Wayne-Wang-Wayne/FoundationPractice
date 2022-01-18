package com.webview.viewmodel

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import com.setDDG.basicClass.BaseViewModel

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
