package com.setDDG.recyclerViewFunction

import androidx.lifecycle.MutableLiveData
import com.setDDG.api.APIService
import com.setDDG.basicClass.BaseViewModel
import com.setDDG.recyclerViewFunction.model.BaseFormatPokeModel
import com.setDDG.recyclerViewFunction.model.pokeDetailModel.PokeDetailModel
import io.reactivex.observers.DisposableSingleObserver


class RecyclerViewFunctionViewModel : BaseViewModel() {
    val pokeFormatList = MutableLiveData<ArrayList<BaseFormatPokeModel>>()

    fun fetchPokeData() {
        isLoading.value = true
        addDisposable(APIService.getPokeDetailData(),
            object : DisposableSingleObserver<ArrayList<PokeDetailModel>>() {
                override fun onSuccess(t: ArrayList<PokeDetailModel>) {
                    formatData(t)
                }

                override fun onError(e: Throwable) {
                    errorMessage.value = defaultErrorMessage
                    isLoading.value = false
                }
            })


    }

    private fun formatData(pokeDetailList: ArrayList<PokeDetailModel>) {

        addDisposable(APIService.formatPokeData(pokeDetailList),
            object : DisposableSingleObserver<ArrayList<BaseFormatPokeModel>>() {
                override fun onSuccess(t: ArrayList<BaseFormatPokeModel>) {
                    pokeFormatList.value = t
                    isLoading.value = false
                }

                override fun onError(e: Throwable) {
                    errorMessage.value = defaultErrorMessage
                    isLoading.value = false
                }
            })


    }
}