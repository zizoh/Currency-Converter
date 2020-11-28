package com.zizohanto.android.currencyconverter.remote.impl

import com.zizohanto.android.currencyconverter.data.contract.remote.ConverterRemote
import com.zizohanto.android.currencyconverter.data.models.HistoricalDataEntity
import com.zizohanto.android.currencyconverter.remote.ApiService
import com.zizohanto.android.currencyconverter.remote.mapper.HistoricalDataRemoteModelMapper
import com.zizohanto.android.currencyconverter.remote.mapper.SymbolRemoteModelMapper
import com.zizohanto.android.currencyconverter.remote.model.HistoricalDataRemoteModel
import com.zizohanto.android.currencyconverter.remote.model.SymbolRemoteModel
import javax.inject.Inject

class ConverterRemoteImpl @Inject constructor(
    private val apiService: ApiService,
    private val symbolRemoteModelMapper: SymbolRemoteModelMapper,
    private val historicalDataRemoteModelMapper: HistoricalDataRemoteModelMapper
) : ConverterRemote {

    override suspend fun getSymbols(): List<String> {
        val symbols: SymbolRemoteModel = apiService.getSymbols()
        return symbolRemoteModelMapper.mapFromModel(symbols).symbols
    }

    override suspend fun getHistoricalData(
        date: String,
        base: String,
        target: String
    ): HistoricalDataEntity {
        val historicalDataEntity: HistoricalDataRemoteModel =
            apiService.getHistoricalData(date, symbols = "$base,$target")
        return historicalDataRemoteModelMapper.mapFromModel(historicalDataEntity)
    }
}