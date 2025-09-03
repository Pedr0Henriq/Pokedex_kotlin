package com.example.testekotlin.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.testekotlin.database.PokeDBDao


@Database(entities = [PokeDB::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase(){

    abstract fun pokemonDao() : PokeDBDao

}