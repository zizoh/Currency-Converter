package com.zizohanto.android.currencyconverter.data.contract.cache

import com.zizohanto.android.currencyconverter.data.models.HistoricalDataEntity

/**
 * Created by zizoh on 25/November/2020.
 */

interface ConverterCache {

    suspend fun getSymbols(): List<String>

    suspend fun saveSymbols(symbols: List<String>)

    suspend fun getHistoricalData(
        base: String,
        target: String
    ): HistoricalDataEntity?

    suspend fun saveHistoricalData(historicalData: HistoricalDataEntity)
}