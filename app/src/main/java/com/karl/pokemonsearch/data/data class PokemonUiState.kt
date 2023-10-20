package com.karl.pokemonsearch.data

import com.karl.pokemonsearch.GetPokemonQuery

data class PokemonUiState(
//    val offset: Int? = 0,
    val pokemonSpeciesList: List<GetPokemonQuery.Pokemon_v2_pokemonspecy> = emptyList(),
    var searchQuery: String = ""
)
