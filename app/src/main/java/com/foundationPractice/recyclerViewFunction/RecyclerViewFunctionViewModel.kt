package com.foundationPractice.recyclerViewFunction

import androidx.lifecycle.MutableLiveData
import com.foundationPractice.api.APIService
import com.foundationPractice.basicClass.BaseViewModel
import com.foundationPractice.recyclerViewFunction.model.BaseFormatPokeModel
import com.foundationPractice.recyclerViewFunction.model.pokeDetailModel.PokeDetailModel
import io.reactivex.observers.DisposableSingleObserver


class RecyclerViewFunctionViewModel : BaseViewModel() {
    val pokeFormatList = MutableLiveData<ArrayList<BaseFormatPokeModel>>()
    var count = 1
    var pokeDetailList: ArrayList<PokeDetailModel> = ArrayList<PokeDetailModel>()

    fun fetchPokeData() {
        isLoading.value = true
        addDisposable(APIService.getPokeDetailData(count),
            object : DisposableSingleObserver<PokeDetailModel>() {
                override fun onSuccess(t: PokeDetailModel) {

                    if (count <= 9) {
                        pokeDetailList.add(t)
                        count++
                        fetchPokeData()
                    } else {
                        formatData(pokeDetailList)
                        count = 1
                    }

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