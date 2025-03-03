package com.zizohanto.android.currencyconverter.data.models

/**
 * Created by zizoh on 25/November/2020.
 */

data class HistoricalDataEntity(
    val id: String,
    val timestamp: Long,
    val base: String,
    val date: String,
    val oneEuroToBaseRate: Double,
    val oneEuroToTargetRate: Double
)