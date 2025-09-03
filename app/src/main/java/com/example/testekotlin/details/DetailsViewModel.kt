package com.example.testekotlin.details

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testekotlin.database.PokeDBDao
import com.example.testekotlin.database.PokeDB
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val pokemonDAO: PokeDBDao,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _pokemon = MutableStateFlow<PokeDB?>(null)
    val pokemon = _pokemon.asStateFlow()

    init {
        val pokemonId = savedStateHandle.get<Long>("id")

        Log.i("Id do pokemon", "$pokemonId")

        if(pokemonId !=null){
            getDetails(pokemonId)
        }
    }

    private fun getDetails(pokemonId:Long){
        viewModelScope.launch {
            _pokemon.value = pokemonDAO.findById(pokemonId)
            Log.i("Pokemon","${_pokemon.value}")
        }
    }

    fun deletePokemon() {
        viewModelScope.launch {
            pokemonDAO.deletePokemons(_pokemon.value!!)
            _pokemon.value = null
        }
    }

}