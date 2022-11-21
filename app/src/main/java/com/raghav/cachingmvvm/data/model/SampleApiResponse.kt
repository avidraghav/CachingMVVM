package com.raghav.cachingmvvm.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "response")
data class SampleApiResponse(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val results: List<SampleApiResponseItem>
)

data class SampleApiResponseItem(
    val id: String,
    val image: String,
    val launch_service_provider: LaunchServiceProvider,
    val name: String,
    val net: String,
    val rocket: Rocket,
    val status: Status,
)

data class Status(
    val abbrev: String,
    val description: String,
    val id: Int,
    val name: String
)

data class Rocket(
    val configuration: Configuration,
    val id: Int
)

data class Configuration(
    val family: String,
    val full_name: String,
    val id: Int,
    val name: String,
    val url: String,
    val variant: String
)

data class LaunchServiceProvider(
    val id: Int,
    val name: String,
    val type: String,
    val url: String
)


