package com.example.testekotlin.database

import androidx.room.TypeConverter
import com.example.testekotlin.pokemon.AbilityWrapper
import com.example.testekotlin.pokemon.TypeWrapper
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

    @TypeConverter
    fun fromAbilitiesList(abilities: List<AbilityWrapper>?): String?{
        if(abilities ==null) return null
        val type = object : TypeToken<List<AbilityWrapper>>(){}.type
        return Gson().toJson(abilities,type)
    }

    @TypeConverter
    fun toAbilitiesList(abilitiesString: String?): List<AbilityWrapper>?{
        if(abilitiesString ==null) return null
        val type = object : TypeToken<List<AbilityWrapper>>() {}.type
        return Gson().fromJson(abilitiesString,type)
    }

    @TypeConverter
    fun fromTypesList(types: List<TypeWrapper>?): String?{
        if(types ==null) return null
        val type = object : TypeToken<List<TypeWrapper>>(){}.type
        return Gson().toJson(types,type)
    }

    @TypeConverter
    fun toTypesList(typesString: String?): List<TypeWrapper>?{
        if(typesString ==null) return null
        val type = object : TypeToken<List<TypeWrapper>>(){}.type
        return Gson().fromJson(typesString,type)
    }
}