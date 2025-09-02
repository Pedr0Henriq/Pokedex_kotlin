package com.example.testekotlin.pokemon

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.testekotlin.database.Converters
import com.example.testekotlin.database.PokeDB


@Database(entities = [PokeDB::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase(){

    abstract fun pokemonDao() : PokeDBDao

}