package com.zizohanto.android.currencyconverter.domain.repository

import com.zizohanto.android.currencyconverter.domain.factory.DomainFactory
import com.zizohanto.android.currencyconverter.domain.factory.DomainFactory.makeHistoricalData
import com.zizohanto.android.currencyconverter.domain.factory.DomainFactory.makeHistoricalDataForPeriod
import com.zizohanto.android.currencyconverter.domain.models.HistoricalData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ConverterRepositoryImplFake @Inject constructor() : ConverterRepository {

    override fun getSymbols(): Flow<List<String>> {
        return flow {
            emit(DomainFactory.getSymbols())
        }
    }

    override fun getRates(
        date: String,
        amount: Double,
        base: String,
        target: String
    ): Flow<HistoricalData> {
        return flow {
            emit(makeHistoricalData())
        }
    }

    override suspend fun getRatesWithinPeriod(
        dates: List<String>,
        base: String,
        target: String
    ): List<HistoricalData> {
        return makeHistoricalDataForPeriod(dates)
    }

}