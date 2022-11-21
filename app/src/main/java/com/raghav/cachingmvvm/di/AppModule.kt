package com.raghav.cachingmvvm.di

import android.content.Context
import androidx.room.Room
import com.raghav.cachingmvvm.data.database.AppDatabase
import com.raghav.cachingmvvm.data.remote.SampleApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideApi(retrofit: Retrofit): SampleApi {
        return retrofit.create(SampleApi::class.java)
    }

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit =
        Retrofit.Builder().baseUrl("https://ll.thespacedevs.com/2.2.0/launch/").build()

    @Singleton
    @Provides
    fun provideTaskDatabase(
        @ApplicationContext context: Context,
    ) = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "database.db"
    ).fallbackToDestructiveMigration()
        .build()

    @Singleton
    @Provides
    fun provideTaskDao(database: AppDatabase) = database.dao()
}