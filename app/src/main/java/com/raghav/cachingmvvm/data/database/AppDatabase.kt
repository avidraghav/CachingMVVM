package com.raghav.cachingmvvm.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.raghav.cachingmvvm.data.model.SampleApiResponse

@Database(
    entities = [SampleApiResponse::class],
    version = 1
)
@TypeConverters(TypeConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun dao(): AppDao
}