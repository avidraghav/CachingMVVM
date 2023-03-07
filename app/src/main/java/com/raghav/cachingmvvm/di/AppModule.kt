package com.raghav.cachingmvvm.di

import android.content.Context
import androidx.room.Room
import com.raghav.cachingmvvm.data.database.AppDatabase
import com.raghav.cachingmvvm.data.remote.SampleApi
import com.raghav.cachingmvvm.utils.AppApplication
import com.raghav.cachingmvvm.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofitClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        val level = logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient.Builder().addInterceptor(level).build()
    }

    @Singleton
    @Provides
    @Named(Constants.SPACE_FLIGHT_API)
    fun provideRetrofitForSpaceFlightApi(client: OkHttpClient): Retrofit =
        Retrofit.Builder().baseUrl(Constants.BASE_URL_SPACEFLIGHT)
            .addConverterFactory(GsonConverterFactory.create()).client(client).build()

    @Singleton
    @Provides
    fun provideSpaceFlightApi(
        @Named(Constants.SPACE_FLIGHT_API) retrofit: Retrofit
    ): SampleApi {
        return retrofit.create(SampleApi::class.java)
    }

    @Singleton
    @Provides
    fun provideTaskDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "database.db"
    ).fallbackToDestructiveMigration()
        .build()

    @Singleton
    @Provides
    fun provideTaskDao(database: AppDatabase) = database.dao()

    @Singleton
    @Provides
    fun providesApplication() = AppApplication()
}
