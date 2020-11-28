package com.zizohanto.android.currencyconverter.cache.impl

import com.zizohanto.android.currencyconverter.cache.mappers.HistoricalDataCacheModelMapper
import com.zizohanto.android.currencyconverter.cache.models.HistoricalDataCacheModel
import com.zizohanto.android.currencyconverter.cache.room.HistoricalDataDao
import com.zizohanto.android.currencyconverter.data.contract.cache.ConverterCache
import com.zizohanto.android.currencyconverter.data.models.HistoricalDataEntity
import javax.inject.Inject

class ConverterCacheImpl @Inject constructor(
    private val historicalDataDao: HistoricalDataDao,
    private val mapper: HistoricalDataCacheModelMapper
) : ConverterCache {

    override suspend fun getSymbols(): List<String> {
        return emptyList()
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
}