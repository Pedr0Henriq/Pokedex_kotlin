package com.example.testekotlin.pokemon

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class PokemonEntity(
    val id:Int,
    val name:String,
    val height: Int,
    val weight: Int,
    val abilities: List<AbilityWrapper>,
    val types: List<TypeWrapper>,
    val sprites: Sprites
)

@Serializable
data class AbilityWrapper(
    val ability: Ability,
    @SerialName("is_hidden")
    val isHidden: Boolean,
    val slot: Int
)

@Serializable
data class Ability(
    val name: String,
    val url: String
)

@Serializable
data class TypeWrapper(
    val slot: Int,
    val type: Type
)

@Serializable
data class Type(
    val name: String,
    val url: String
)

@Serializable
data class Sprites(
    @SerialName("front_default")
    val frontDefault: String?,
    @SerialName("back_default")
    val backDefault: String?,
    @SerialName("front_shiny")
    val frontShiny: String?,
    @SerialName("back_shiny")
    val backShiny: String?,
    val other: OtherSprites
)

@Serializable
data class OtherSprites(
    @SerialName("official-artwork")
    val officialArtwork: OfficialArtwork
)

@Serializable
data class OfficialArtwork(
    @SerialName("front_default")
    val frontDefault: String?,
    @SerialName("front_shiny")
    val frontShiny: String?
)
