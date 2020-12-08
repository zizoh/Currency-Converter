package com.zizohanto.android.currencyconverter.cache.room

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.zizohanto.android.currencyconverter.cache.datafactory.CacheDataFactory
import com.zizohanto.android.currencyconverter.cache.models.SymbolCacheModel
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by zizoh on 07/December/2020.
 */

@RunWith(AndroidJUnit4::class)
class SymbolDaoTest {

    private lateinit var sut: SymbolDao
    private lateinit var database: CurrencyConverterDatabase

    @Before
    fun setup() = runBlocking {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            CurrencyConverterDatabase::class.java
        ).allowMainThreadQueries().build()

        sut = database.symbolDao
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun `insertSymbols inserts symbols`() = runBlocking {
        val expected = CacheDataFactory.makeSymbolCacheModels(10)

        sut.insertSymbols(expected)

        val actual: List<SymbolCacheModel> = sut.getSymbols()
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `getSymbols returns empty list if no symbols saved`() = runBlocking {
        val actual = sut.getSymbols().isEmpty()

        assertThat(actual).isEqualTo(true)
    }

    @Test
    fun `getSymbols returns saved symbols`() = runBlocking {
        val expected = CacheDataFactory.makeSymbolCacheModels(10)
        sut.insertSymbols(expected)

        val actual: List<SymbolCacheModel> = sut.getSymbols()

        assertThat(actual).isEqualTo(expected)
    }
}