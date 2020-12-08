package com.zizohanto.android.currencyconverter.data.fakes

import com.zizohanto.android.currencyconverter.data.contract.cache.ConverterCache
import com.zizohanto.android.currencyconverter.data.models.HistoricalDataEntity

/**
 * Created by zizoh on 07/December/2020.
 */

class ConverterCacheFake : ConverterCache {

    private val symbols: ArrayList<String> = ArrayList()

    private lateinit var historicalData: HistoricalDataEntity

    override suspend fun getSymbols(): List<String> {
        return symbols
    }

    override suspend fun saveSymbols(symbols: List<String>) {
        this.symbols.addAll(symbols)
    }

    override suspend fun getHistoricalData(base: String, target: String): HistoricalDataEntity? {
        return historicalData
    }

    override suspend fun saveHistoricalData(historicalData: HistoricalDataEntity) {
        this.historicalData = historicalData
    }

}