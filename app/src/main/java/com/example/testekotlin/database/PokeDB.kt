package com.example.testekotlin.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.testekotlin.pokemon.AbilityWrapper
import com.example.testekotlin.pokemon.TypeWrapper

@Entity(tableName = "pokemons")
data class PokeDB(
    @PrimaryKey(autoGenerate = true) val id:Long=0,
    val name:String,
    val height: Int,
    val weight: Int,
    val abilities: List<AbilityWrapper>,
    val types: List<TypeWrapper>,
    val photo : String?,
    val isFavorite: Boolean
)