package com.raghav.cachingmvvm.repository

import android.content.Context
import com.raghav.cachingmvvm.data.database.AppDao
import com.raghav.cachingmvvm.data.remote.SampleApi
import com.raghav.cachingmvvm.utils.Helpers.Companion.isConnectedToNetwork
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class Repository @Inject constructor(
    private val api: SampleApi,
    private val dao: AppDao,
    @ApplicationContext
    private val context: Context
) {

    suspend fun getArticles() =
        withContext(Dispatchers.IO) {
            if (context.isConnectedToNetwork()) {
                refreshArticlesInDb()
            }
            dao.getArticlesFromDb() ?: emptyList()
        }

    suspend fun refreshArticlesInDb(): Boolean {
        val response = api.getArticles()
        return if (response.isSuccessful) {
            response.body()?.let { dao.saveArticles(it) }
            true
        } else {
            false
        }
    }
}
