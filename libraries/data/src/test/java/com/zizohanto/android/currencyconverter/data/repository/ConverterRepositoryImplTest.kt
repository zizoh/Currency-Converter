package com.zizohanto.android.currencyconverter.data.repository

import com.google.common.truth.Truth.assertThat
import com.zizohanto.android.currencyconverter.data.factory.DataFactory.makeHistoricalDataEntity
import com.zizohanto.android.currencyconverter.data.fakes.ConverterCacheFake
import com.zizohanto.android.currencyconverter.data.fakes.ConverterRemoteFake
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Test

/**
 * Created by zizoh on 07/December/2020.
 */

class ConverterRepositoryImplTest {

    private val converterCache = ConverterCacheFake()
    private val converterRemote = ConverterRemoteFake()

    private val sut = ConverterRepositoryImpl(converterRemote, converterCache)

    private val symbols = listOf("USD", "NGN")

    @Test
    fun `getSymbols returns symbols from remote if no symbols stored in cache`() = runBlocking {
        converterRemote.saveSymbols(symbols)

        val actual = sut.getSymbols().first()

        assertThat(actual.size).isEqualTo(2)
    }

    @Test
    fun `getSymbols returns symbols from cache if symbols are stored in cache`() = runBlocking {
        val expected = listOf("ABC", "DEF", "GHI", "JKL")
        converterCache.saveSymbols(expected)
        converterRemote.saveSymbols(symbols)

        val actual = sut.getSymbols().first()

        assertThat(actual.size).isEqualTo(4)
    }

    @Test
    fun `getHistoricalData returns historical data`() = runBlocking {
        val historicalDataEntity = makeHistoricalDataEntity()
        val expectedRate = 669302.6345121895

        val actual = sut.getHistoricalData(historicalDataEntity, 1759.0)

        assertThat(actual.convertedRate).isEqualTo(expectedRate)
    }

    @Test
    fun `getConversion converts rate correctly`() = runBlocking {
        val historicalDataEntity = makeHistoricalDataEntity()
        val expectedRate = 669302.6345121895

        val actual: Double = sut.getConversion(historicalDataEntity, 1759.0)

        assertThat(actual).isEqualTo(expectedRate)
    }
}