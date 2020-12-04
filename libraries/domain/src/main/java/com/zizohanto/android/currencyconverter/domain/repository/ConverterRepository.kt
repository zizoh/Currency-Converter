package com.zizohanto.android.currencyconverter.domain.repository

import com.zizohanto.android.currencyconverter.domain.models.HistoricalData
import kotlinx.coroutines.flow.Flow

/**
 * Created by zizoh on 25/November/2020.
 */

interface ConverterRepository {

    fun getSymbols(): Flow<List<String>>

    fun getRates(
        date: String,
        amount: Double,
        base: String,
        target: String
    ): Flow<HistoricalData>

    fun getRatesWithinPeriod(
        dates: List<String>,
        base: String,
        target: String
    ): Flow<List<HistoricalData>>
}