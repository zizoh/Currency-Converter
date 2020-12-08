package com.zizohanto.android.currencyconverter.data.fakes

import com.zizohanto.android.currencyconverter.data.contract.remote.ConverterRemote
import com.zizohanto.android.currencyconverter.data.factory.DataFactory.makeHistoricalDataEntity
import com.zizohanto.android.currencyconverter.data.models.HistoricalDataEntity

/**
 * Created by zizoh on 07/December/2020.
 */

class ConverterRemoteFake : ConverterRemote {

    private val symbols: ArrayList<String> = ArrayList()

    override suspend fun getSymbols(): List<String> {
        return symbols
    }

    override suspend fun getHistoricalData(
        date: String,
        base: String,
        target: String
    ): HistoricalDataEntity {
        return makeHistoricalDataEntity()
    }

    fun saveSymbols(symbols: List<String>) {
        this.symbols.addAll(symbols)
    }
}