package com.zizohanto.android.currencyconverter.data.repository

import com.zizohanto.android.currencyconverter.data.contract.cache.ConverterCache
import com.zizohanto.android.currencyconverter.data.contract.remote.ConverterRemote
import com.zizohanto.android.currencyconverter.data.models.HistoricalDataEntity
import com.zizohanto.android.currencyconverter.domain.models.HistoricalData
import com.zizohanto.android.currencyconverter.domain.repository.ConverterRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ConverterRepositoryImpl @Inject constructor(
    private val converterRemote: ConverterRemote,
    private val converterCache: ConverterCache
) : ConverterRepository {

    override fun getSymbols(): Flow<List<String>> {
        return flow {
            val symbols: List<String> = converterRemote.getSymbols()
            emit(symbols)
        }
    }

    override fun getRates(
        date: String,
        amount: Double,
        base: String,
        target: String
    ): Flow<HistoricalData> {
        return flow {
            val dataFromCache: HistoricalDataEntity? =
                converterCache.getHistoricalData(base, target)
            if (dataFromCache != null) {
                val historicalData = getHistoricalData(dataFromCache, amount)
                emit(historicalData)
            }
            val dataFromRemote: HistoricalDataEntity =
                converterRemote.getHistoricalData(date, base, target)
            val historicalData = getHistoricalData(dataFromRemote, amount)
            emit(historicalData)
        }
    }

    private fun getHistoricalData(
        historicalDataFromCache: HistoricalDataEntity,
        amount: Double
    ): HistoricalData {
        val convertedRate: Double = getConversion(historicalDataFromCache, amount)
        return HistoricalData(convertedRate, historicalDataFromCache.timestamp)
    }

    private fun getConversion(
        historicalData: HistoricalDataEntity,
        amount: Double
    ): Double {
        val oneEuroToTargetRate = historicalData.oneEuroToTargetRate
        val oneEuroToBaseRate = historicalData.oneEuroToBaseRate
        return amount * (oneEuroToTargetRate / oneEuroToBaseRate)
    }
}