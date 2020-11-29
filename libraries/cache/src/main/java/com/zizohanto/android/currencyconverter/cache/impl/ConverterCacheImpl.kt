package com.zizohanto.android.currencyconverter.cache.impl

import com.zizohanto.android.currencyconverter.cache.mappers.HistoricalDataCacheModelMapper
import com.zizohanto.android.currencyconverter.cache.mappers.SymbolCacheModelMapper
import com.zizohanto.android.currencyconverter.cache.models.HistoricalDataCacheModel
import com.zizohanto.android.currencyconverter.cache.models.SymbolCacheModel
import com.zizohanto.android.currencyconverter.cache.room.HistoricalDataDao
import com.zizohanto.android.currencyconverter.cache.room.SymbolDao
import com.zizohanto.android.currencyconverter.data.contract.cache.ConverterCache
import com.zizohanto.android.currencyconverter.data.models.HistoricalDataEntity
import javax.inject.Inject

class ConverterCacheImpl @Inject constructor(
    private val historicalDataDao: HistoricalDataDao,
    private val symbolDao: SymbolDao,
    private val mapper: HistoricalDataCacheModelMapper,
    private val symbolCacheModelMapper: SymbolCacheModelMapper
) : ConverterCache {

    override suspend fun getSymbols(): List<String> {
        val symbols: List<SymbolCacheModel> = symbolDao.getSymbols()
        return symbolCacheModelMapper.mapToEntityList(symbols)
    }

    override suspend fun saveSymbols(symbols: List<String>) {
        val symbolsCacheModels: List<SymbolCacheModel> = symbolCacheModelMapper.mapToModelList(symbols)
        return symbolDao.insertSymbols(symbolsCacheModels)
    }

    override suspend fun getHistoricalData(
        base: String,
        target: String
    ): HistoricalDataEntity? {
        val historicalData: HistoricalDataCacheModel? =
            historicalDataDao.getHistoricalDataWithId("$base$target")
        return if (historicalData == null) null
        else mapper.mapToEntity(historicalData)
    }

    override suspend fun saveHistoricalData(historicalData: HistoricalDataEntity) {
        historicalDataDao.insertHistoricalData(mapper.mapToModel(historicalData))
    }
}