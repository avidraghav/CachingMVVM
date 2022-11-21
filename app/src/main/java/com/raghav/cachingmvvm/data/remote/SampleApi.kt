package com.raghav.cachingmvvm.data.remote

import com.raghav.cachingmvvm.data.model.SampleApiResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface SampleApi {

    @GET("upcoming")
    suspend fun getLaunches(
        @Query("offset")
        offset : Int =0
    ): Response<SampleApiResponse>
}