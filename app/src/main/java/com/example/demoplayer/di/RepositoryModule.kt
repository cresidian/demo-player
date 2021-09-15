package com.example.demoplayer.di

import com.example.demoplayer.networking.ItunesApi
import com.example.demoplayer.repositories.SongsRepositories
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    fun provideSongsRepositories(
        api: ItunesApi
    ) = SongsRepositories(api)

}