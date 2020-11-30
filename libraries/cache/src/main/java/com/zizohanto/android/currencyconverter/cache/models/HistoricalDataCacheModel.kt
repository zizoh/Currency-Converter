package com.zizohanto.android.currencyconverter.cache.models

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by zizoh on 28/November/2020.
 */

@Entity(tableName = "historical_data")
data class HistoricalDataCacheModel(
    @PrimaryKey
    val id: String,
    val timestamp: Long,
    val base: String,
    val date: String,
    val oneEuroToBaseRate: Double,
    val oneEuroToTargetRate: Double
)