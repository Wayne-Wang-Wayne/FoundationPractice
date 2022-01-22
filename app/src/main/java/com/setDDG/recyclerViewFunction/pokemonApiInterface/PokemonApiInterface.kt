package com.setDDG.recyclerViewFunction.pokemonApiInterface

import com.setDDG.recyclerViewFunction.model.pokeDetailModel.PokeDetailModel
import com.setDDG.recyclerViewFunction.model.pokeModel.PokeModel
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface PokeDetailInfo{
    @GET("/api/v2/pokemon/{id}/")
    fun getPokeDetailInfo(@Path("id") id: Int): PokeDetailModel
}