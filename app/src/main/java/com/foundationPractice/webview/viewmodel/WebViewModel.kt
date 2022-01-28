package com.foundationPractice.webview.viewmodel

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import com.foundationPractice.basicClass.BaseViewModel

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
