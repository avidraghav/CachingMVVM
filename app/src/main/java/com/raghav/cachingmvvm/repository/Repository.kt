package com.raghav.cachingmvvm.repository

import com.raghav.cachingmvvm.data.database.AppDao
import com.raghav.cachingmvvm.data.remote.SampleApi
import javax.inject.Inject

class Repository @Inject constructor(
    private val api: SampleApi,
    private val dao: AppDao
) {

}