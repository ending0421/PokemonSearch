package com.karl.pokemonsearch

import androidx.lifecycle.ViewModel
import com.karl.pokemonsearch.data.PokemonUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SpeciesViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(PokemonUiState())
    val uiState: StateFlow<PokemonUiState> = _uiState.asStateFlow()

//    fun updateOffsetState(offset: Int) {
//        _uiState.update { currentState ->
//            currentState.copy(
//                offset = offset
//            )
//        }
//    }

    fun updateSearchQuery(newQuery: String) {
        _uiState.update { currentState ->
            currentState.copy(
                searchQuery = newQuery
            )
        }
    }
    fun clearPokemonResult(){
        _uiState.update { currentState ->
            currentState.copy(
                pokemonSpeciesList = emptyList()
            )
        }
    }

    fun appendPokemonResultList(newResult: List<GetPokemonQuery.Pokemon_v2_pokemonspecy>) {
        _uiState.update { currentState ->
            currentState.copy(
                pokemonSpeciesList = currentState.pokemonSpeciesList + newResult
            )
        }
    }
}