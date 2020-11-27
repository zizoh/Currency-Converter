package com.zizohanto.android.currencyconverter.data.contract.remote

import com.zizohanto.android.currencyconverter.data.models.HistoricalDataEntity

/**
 * Created by zizoh on 25/November/2020.
 */

interface ConverterRemote {

    suspend fun getSymbols(): List<String>

    suspend fun getHistoricalData(date: String, symbols: List<String>): HistoricalDataEntity
}