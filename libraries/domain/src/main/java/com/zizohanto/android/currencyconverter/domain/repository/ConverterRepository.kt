package com.zizohanto.android.currencyconverter.domain.repository

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
    ): Flow<Double>
}