package com.zizohanto.android.currencyconverter.data.repository

import com.zizohanto.android.currencyconverter.data.contract.remote.ConverterRemote
import com.zizohanto.android.currencyconverter.data.models.HistoricalDataEntity
import com.zizohanto.android.currencyconverter.domain.repository.ConverterRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ConverterRepositoryImpl @Inject constructor(
    private val converterRemote: ConverterRemote
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
    ): Flow<Double> {
        return flow {
            val historicalData: HistoricalDataEntity =
                converterRemote.getHistoricalData(date, listOf(base, target))
            val value: List<Double> = historicalData.rates.values.toList()
            val oneEuroToBaseRate: Double = value[0]
            val oneEuroToTargetRate: Double = value[1]
            val convertedRate = oneEuroToBaseRate * amount * oneEuroToTargetRate
            emit(convertedRate)
        }
    }
}