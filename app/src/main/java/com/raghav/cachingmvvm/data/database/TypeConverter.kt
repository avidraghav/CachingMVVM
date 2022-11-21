package com.raghav.cachingmvvm.data.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.raghav.cachingmvvm.data.model.SampleApiResponseItem

object TypeConverter {

    private val gson = Gson()

    @TypeConverter
    fun fromList(value: List<SampleApiResponseItem>): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toList(value: String): List<SampleApiResponseItem> {
        return gson.fromJson(
            value,
            object : TypeToken<List<SampleApiResponseItem>>() {
            }.type
        )
    }

}