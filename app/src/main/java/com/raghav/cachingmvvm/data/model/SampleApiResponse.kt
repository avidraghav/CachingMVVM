package com.raghav.cachingmvvm.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

class ArticlesResponse : ArrayList<ArticlesResponseItem>()

@Entity(tableName = "cached_response")
data class ArticlesResponseItem(
    @PrimaryKey
    val id: Int,
    val imageUrl: String,
    val newsSite: String,
    val publishedAt: String,
    val summary: String,
    val title: String,
    val updatedAt: String,
    val url: String
) : Serializable
