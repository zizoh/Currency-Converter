package com.zizohanto.android.currencyconverter.cache.impl

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.zizohanto.android.currencyconverter.cache.datafactory.CacheDataFactory.makeHistoricalDataEntity
import com.zizohanto.android.currencyconverter.cache.datafactory.CacheDataFactory.makeRandomString
import com.zizohanto.android.currencyconverter.cache.datafactory.CacheDataFactory.makeRandomStringList
import com.zizohanto.android.currencyconverter.cache.mappers.HistoricalDataCacheModelMapper
import com.zizohanto.android.currencyconverter.cache.mappers.SymbolCacheModelMapper
import com.zizohanto.android.currencyconverter.cache.room.CurrencyConverterDatabase
import com.zizohanto.android.currencyconverter.data.contract.cache.ConverterCache
import com.zizohanto.android.currencyconverter.data.models.HistoricalDataEntity
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by zizoh on 07/December/2020.
 */

@RunWith(AndroidJUnit4::class)
class ConverterCacheImplTest {

    private lateinit var sut: ConverterCache
    private lateinit var database: CurrencyConverterDatabase

    @Before
    fun setup() = runBlocking {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            CurrencyConverterDatabase::class.java
        ).allowMainThreadQueries().build()

        sut = ConverterCacheImpl(
            database.historicalDataDao,
            database.symbolDao,
            HistoricalDataCacheModelMapper(),
            SymbolCacheModelMapper()
        )
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun `getSymbols returns empty list if symbols are not saved`() = runBlocking {
        val actual = sut.getSymbols().isEmpty()

        assertThat(actual).isEqualTo(true)
    }

    @Test
    fun `getSymbols returns saved symbols`() = runBlocking {
        val expected = makeRandomStringList(10)
        sut.saveSymbols(expected)

        val actual: List<String> = sut.getSymbols()

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `saveSymbols saves symbols`() = runBlocking {
        val expected = makeRandomStringList(10)
        sut.saveSymbols(expected)

        val actual: List<String> = sut.getSymbols()

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `getHistoricalData returns null if no historical data with that id saved`() = runBlocking {
        val base = makeRandomString()
        val target = makeRandomString()

        val actual: HistoricalDataEntity? = sut.getHistoricalData(base, target)

        assertThat(actual).isEqualTo(null)
    }

    @Test
    fun `getHistoricalData returns saved historical data`() = runBlocking {
        val base = makeRandomString()
        val target = makeRandomString()
        val id = "$base$target"
        val expected: HistoricalDataEntity = makeHistoricalDataEntity().copy(id = id, base = base)
        sut.saveHistoricalData(expected)

        val actual: HistoricalDataEntity? = sut.getHistoricalData(base, target)

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `saveHistoricalData saves data`() = runBlocking {
        val base = makeRandomString()
        val target = makeRandomString()
        val id = "$base$target"
        val expected: HistoricalDataEntity = makeHistoricalDataEntity().copy(id = id, base = base)
        sut.saveHistoricalData(expected)

        val actual: HistoricalDataEntity? = sut.getHistoricalData(base, target)

        assertThat(actual).isEqualTo(expected)
    }
}