package com.setDDG.recyclerViewFunction.model.pokeModel

data class PokeModel(
    val count: Int,
    val next: String,
    val previous: Any,
    val results: List<Result>
)