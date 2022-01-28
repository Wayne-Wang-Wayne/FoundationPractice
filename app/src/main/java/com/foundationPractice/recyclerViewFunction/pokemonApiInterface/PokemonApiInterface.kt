package com.foundationPractice.recyclerViewFunction.pokemonApiInterface

import com.foundationPractice.recyclerViewFunction.model.pokeDetailModel.PokeDetailModel
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface PokeDetailInfo{
    @GET("/api/v2/pokemon/{id}/")
    fun getPokeDetailInfo(@Path("id") id: Int): Single<PokeDetailModel>
}