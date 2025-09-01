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

    companion object {
        @Volatile
        private var INSTANCE : AppDatabase? = null

        fun getDatabase(context: Context) : AppDatabase {
            val tempInstance = INSTANCE

            if(tempInstance !=null){
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                   context =  context.applicationContext,
                   klass =   AppDatabase::class.java,
                   name =  "banco_de_dados"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}