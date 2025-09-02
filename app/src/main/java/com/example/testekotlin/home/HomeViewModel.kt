package com.example.testekotlin.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testekotlin.api.ApiClient
import com.example.testekotlin.database.PokeDB
import com.example.testekotlin.pokemon.PokeDBDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val pokemonDAO: PokeDBDao,
    private val pokeApi: ApiClient) : ViewModel() {

    val pokemons: StateFlow<List<PokeDB>> = pokemonDAO.getAll().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Exposes the favorites list reactively
    val favoritePokemons: StateFlow<List<PokeDB>> = pokemonDAO.getAllFavorite().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun findPokemon(id: Long? = null, name: String? = null) {
           viewModelScope.launch {
               try {
                   val pokemon = when{
                       name!=null -> pokemonDAO.findByName(name)
                       id!=null -> pokemonDAO.findById(id)
                       else -> null
                   }

                       fetchAndSavePokemon(query = name, id = id)


               } catch (e: Exception){
                   Log.e("PokemonViewModel","Erro ao procurar pokemon na API",e)

               }
           }

    }



    suspend fun fetchAndSavePokemon(query: String? = null, id: Long? = null){
        viewModelScope.launch{
            try {

                val pokemonEntity = when {
                    query != null -> pokeApi.retrofit.findPokemonByName(query)
                    id != null -> pokeApi.retrofit.findPokemonById(id.toInt())
                    else -> null
                }
                if(pokemonEntity == null) return@launch
                val pokedbId = pokemonDAO.insertPokemons(
                    PokeDB(
                        name = pokemonEntity.name,
                        isFavorite = false,
                        height = pokemonEntity.height,
                        weight = pokemonEntity.weight,
                        photo = pokemonEntity.sprites.frontDefault,
                        abilities = pokemonEntity.abilities,
                        types = pokemonEntity.types
                    )
                )

            } catch (e: Exception) {
                Log.e("PokemonViewModel", "Erro ao buscar/salvar Pokemon", e)
                null
            }
        }

    }

    fun updateFavorite(pokemon: PokeDB, favorite: Boolean){
        viewModelScope.launch {
            try {
                val updatedPokemon = pokemon.copy(
                    isFavorite = favorite
                )
                pokemonDAO.updatePokemons(updatedPokemon)
            } catch (e: Exception){
                Log.e("HomeViewModel", "Erro ao atualizar status de favorito: $e")
            }
        }
    }
}