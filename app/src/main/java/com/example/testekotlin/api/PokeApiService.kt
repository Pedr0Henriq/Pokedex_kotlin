package com.example.testekotlin.api

import com.example.testekotlin.pokemon.PokemonEntity
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.Retrofit
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.http.GET
import retrofit2.http.Path

interface PokeApiService {

    @GET("pokemon/{name}/")
    suspend fun findPokemonByName(@Path("name") name:String) : PokemonEntity

    @GET("pokemon/{id}/")
    suspend fun findPokemonById(@Path("id") id:Int) : PokemonEntity
}

object ApiClient{
    private const val BASE_URL = "https://pokeapi.co/api/v2/"

    val json = Json { ignoreUnknownKeys = true }
    val contentType = "application/json".toMediaType()

    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(json.asConverterFactory(contentType))
        .build()
        .create(PokeApiService::class.java)
}