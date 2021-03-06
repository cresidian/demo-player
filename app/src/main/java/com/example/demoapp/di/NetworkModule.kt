package com.example.demoapp.di

import android.content.Context
import com.example.demoapp.core.networking.ApiClient
import com.example.demoapp.data.sources.remote.Endpoints
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
    fun provideApiEndpoint(apiClient: Retrofit): Endpoints =
        apiClient.create(Endpoints::class.java)

}