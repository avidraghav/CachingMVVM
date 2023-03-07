package com.raghav.cachingmvvm.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.raghav.cachingmvvm.data.model.ArticlesResponseItem

@Dao
interface AppDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveArticles(articles: List<ArticlesResponseItem>)

    @Query("SELECT * FROM cached_response")
    fun getArticlesFromDb(): List<ArticlesResponseItem>?
}
