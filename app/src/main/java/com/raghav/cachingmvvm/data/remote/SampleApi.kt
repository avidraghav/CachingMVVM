package com.raghav.cachingmvvm.data.remote

import com.raghav.cachingmvvm.data.model.ArticlesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface SampleApi {

    @GET("articles")
    suspend fun getArticles(
        @Query("_start")
        articlesToSkip: Int = 0
    ): Response<ArticlesResponse>
}
