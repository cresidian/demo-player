package com.example.demoplayer.di

import android.content.Context
import com.example.demoplayer.app.DemoPlayer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideApplication(@ApplicationContext application: Context): DemoPlayer {
        return application as DemoPlayer
    }

}