package com.example.testekotlin.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testekotlin.api.ApiClient
import com.example.testekotlin.database.PokeDB
import com.example.testekotlin.pokemon.PokeDBDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val pokemonDAO: PokeDBDao,
    private val pokeApi: ApiClient) : ViewModel() {


    private val _favoritePokemons = MutableStateFlow<List<PokeDB>>(emptyList())
    val favoritePokemons: StateFlow<List<PokeDB>> = _favoritePokemons.asStateFlow()

    private val _pokemons = MutableStateFlow<List<PokeDB>>(emptyList())
    val pokemons: StateFlow<List<PokeDB>> = _pokemons.asStateFlow()

    init {
        getAllPokemons()
    }


    private fun getAllPokemons() {
        viewModelScope.launch {
            _pokemons.value = pokemonDAO.getAll()
            _favoritePokemons.value = pokemonDAO.getAllFavorite()
        }
    }

    fun findPokemon(id: Long? = null, name: String? = null) {
           viewModelScope.launch {
               try {
                   val pokemon = when{
                       name!=null -> pokemonDAO.findByName(name)
                       id!=null -> pokemonDAO.findById(id)
                       else -> null
                   }
                   if(pokemon!=null){
                       _pokemons.value = _pokemons.value + pokemon
                       return@launch
                   }
                       fetchAndSavePokemon(query = name, id = id)


               } catch (e: Exception){
                   Log.e("PokemonViewModel","Erro ao procurar pokemon na API",e)

               }
           }

    }



    suspend fun fetchAndSavePokemon(query: String? = null, id: Long? = null){
        try {

            val pokemonEntity = when {
                query != null -> pokeApi.retrofit.findPokemonByName(query)
                id != null -> pokeApi.retrofit.findPokemonById(id.toInt())
                else -> null
            }
            if(pokemonEntity == null) return
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

           _pokemons.value = _pokemons.value + pokemonDAO.findById(pokedbId)

        } catch (e: Exception) {
            Log.e("PokemonViewModel", "Erro ao buscar/salvar Pokemon", e)
            null
        }
    }

    fun updateFavorite(pokemon: PokeDB, favorite: Boolean){
        viewModelScope.launch {
            try {
                val updatedPokemon = pokemon.copy(
                    isFavorite = favorite
                )
                pokemonDAO.updatePokemons(updatedPokemon)

                _pokemons.value = _pokemons.value.map {
                    if(it.id == updatedPokemon.id) updatedPokemon else it
                }

                if(favorite){
                    _favoritePokemons.value = _favoritePokemons.value + updatedPokemon
                }else{
                    _favoritePokemons.value = _favoritePokemons.value.filter { it.id != updatedPokemon.id }
                }
            } catch (e: Exception){
                Log.e("HomeViewModel", "Erro ao atualizar status de favorito: $e")
            }
        }
    }
}