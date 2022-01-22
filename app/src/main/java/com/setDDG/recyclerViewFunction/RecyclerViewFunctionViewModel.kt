package com.setDDG.recyclerViewFunction

import androidx.lifecycle.MutableLiveData
import com.setDDG.api.APIService
import com.setDDG.basicClass.BaseViewModel
import com.setDDG.recyclerViewFunction.model.pokeModel.PokeModel
import io.reactivex.observers.DisposableSingleObserver


class RecyclerViewFunctionViewModel:BaseViewModel() {
    val pokeBaseInfo = MutableLiveData<PokeModel>()

    fun fetchPokeData(){
        isLoading.value = true
        addDisposable(APIService.getPokeBaseData(),object :DisposableSingleObserver<PokeModel>(){
            override fun onSuccess(t: PokeModel) {
                pokeBaseInfo.value = t
            }

            override fun onError(e: Throwable) {
                errorMessage.value = defaultErrorMessage
            }
        })
    }

    private fun
}