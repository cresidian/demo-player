package com.example.demoplayer.di

import android.content.Context
import com.example.demoplayer.networking.ApiClient
import com.example.demoplayer.networking.IApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideApiClient(
        @ApplicationContext appContext: Context,
    ) = ApiClient(appContext).getApiClient()

    @Singleton
    @Provides
    fun provideApiEndpoint(apiClient: Retrofit): IApi =
        apiClient.create(IApi::class.java)

}