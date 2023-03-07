package com.raghav.cachingmvvm.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.raghav.cachingmvvm.data.model.ArticlesResponseItem

@Database(
    entities = [ArticlesResponseItem::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun dao(): AppDao
}
