package com.zizohanto.android.currencyconverter.remote.model

/**
 * Created by zizoh on 25/November/2020.
 */

data class HistoricalDataRemoteModel(
    val timestamp: Long,
    val base: String,
    val date: String,
    val rates: Any
)