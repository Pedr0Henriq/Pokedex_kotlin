package com.example.testekotlin.pokemon

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun providerAppDatabase(@ApplicationContext context: Context): AppDatabase{
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "banco_de_dados"
        ).build()
    }

    @Provides
    @Singleton
    fun providerPokemonDAO(appDatabase: AppDatabase): PokeDBDao{
        return appDatabase.pokemonDao()
    }
}