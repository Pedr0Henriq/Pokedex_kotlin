package com.example.testekotlin.home

import com.example.testekotlin.api.ApiClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object ApiClientModule {

    @Provides
    @Singleton
    fun providerApiClient() : ApiClient{
        return ApiClient
    }
}