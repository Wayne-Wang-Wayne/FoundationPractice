package com.setDDG.recyclerViewFunction.pokemonApiInterface

import com.setDDG.recyclerViewFunction.model.pokeModel.PokeModel
import io.reactivex.Single
import retrofit2.http.GET

interface PokeBaseInfo{
    @GET("pokemon?limit=200&offset=0")
    fun getPokeBaseInfo(): Single<PokeModel>
}