package com.zizohanto.android.currencyconverter.data.repository

import com.zizohanto.android.currencyconverter.data.contract.cache.ConverterCache
import com.zizohanto.android.currencyconverter.data.contract.remote.ConverterRemote
import com.zizohanto.android.currencyconverter.data.models.HistoricalDataEntity
import com.zizohanto.android.currencyconverter.domain.factory.DomainFactory
import com.zizohanto.android.currencyconverter.domain.models.HistoricalData
import com.zizohanto.android.currencyconverter.domain.repository.ConverterRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ConverterRepositoryImpl @Inject constructor(
    private val converterRemote: ConverterRemote,
    private val converterCache: ConverterCache
) : ConverterRepository {

    override fun getSymbols(): Flow<List<String>> {
        return flow {
            val symbolsCache = converterCache.getSymbols()
            if (symbolsCache.isNotEmpty()) {
                emit(symbolsCache)
                return@flow
            }
            val symbolsRemote: List<String> = converterRemote.getSymbols()
            converterCache.saveSymbols(symbolsRemote)
            emit(symbolsRemote)
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
            val id = "${base}${target}"
            val dataFromRemote: HistoricalDataEntity =
                converterRemote.getHistoricalData(date, base, target).copy(id = id)
            converterCache.saveHistoricalData(dataFromRemote)
            val historicalData = getHistoricalData(dataFromRemote, amount)
            emit(historicalData)
        }
    }

    override suspend fun getRatesWithinPeriod(
        dates: List<String>,
        base: String,
        target: String
    ): List<HistoricalData> {
        // make stubNetworkCall false to make network call to get historical data
        val stubNetworkCall = true
        return if (stubNetworkCall) DomainFactory.makeHistoricalDataForPeriod(dates)
        else makeNetworkCall(dates, base, target)
    }

    private suspend fun makeNetworkCall(
        dates: List<String>,
        base: String,
        target: String
    ): List<HistoricalData> {
        return coroutineScope {
            return@coroutineScope (dates).map { date ->
                async { converterRemote.getHistoricalData(date, base, target) }
            }.awaitAll().map { getHistoricalData(it, 1.0) }
        }
    }

    fun getHistoricalData(
        historicalDataFromCache: HistoricalDataEntity,
        amount: Double
    ): HistoricalData {
        val convertedRate: Double = getConversion(historicalDataFromCache, amount)
        return HistoricalData(convertedRate, historicalDataFromCache.timestamp)
    }

    fun getConversion(
        historicalData: HistoricalDataEntity,
        amount: Double
    ): Double {
        val oneEuroToTargetRate = historicalData.oneEuroToTargetRate
        val oneEuroToBaseRate = historicalData.oneEuroToBaseRate
        return amount * (oneEuroToTargetRate / oneEuroToBaseRate)
    }
}