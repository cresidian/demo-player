package com.example.demoplayer.di

import android.content.Context
import com.example.demoplayer.networking.ApiClient
import com.example.demoplayer.networking.ItunesApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun provideApiClient(
        @ApplicationContext appContext: Context,
    ) = ApiClient(appContext).getApiClient()

    @Provides
    fun provideApiEndpoint(apiClient: Retrofit): ItunesApi =
        apiClient.create(ItunesApi::class.java)

}