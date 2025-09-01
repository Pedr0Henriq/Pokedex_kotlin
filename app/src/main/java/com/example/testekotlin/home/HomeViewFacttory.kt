package com.example.testekotlin.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.testekotlin.api.ApiClient
import com.example.testekotlin.pokemon.PokeDBDao

class HomeViewFacttory(
    private val pokemonDAO: PokeDBDao,
    private val pokeApi: ApiClient
) : ViewModelProvider.Factory{
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(pokemonDAO, pokeApi) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}