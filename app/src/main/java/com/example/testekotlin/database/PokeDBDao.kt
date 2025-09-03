package com.example.testekotlin.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface PokeDBDao {
    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertPokemons(pokemons : PokeDB) : Long

    @Update
    suspend fun updatePokemons(pokemons:PokeDB)

    @Delete
    suspend fun deletePokemons(pokemons:PokeDB)

    @Query("SELECT * FROM pokemons")
    fun getAll(): Flow<List<PokeDB>>

    @Query("SELECT * FROM pokemons WHERE id = :pokemonId")
    suspend fun findById(pokemonId: Long): PokeDB

    @Query("SELECT * FROM pokemons WHERE name = :pokemonName")
    suspend fun findByName(pokemonName:String): PokeDB

    @Query("SELECT * FROM pokemons WHERE isFavorite = 1")
    fun getAllFavorite(): Flow<List<PokeDB>>
}