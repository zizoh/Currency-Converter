package com.zizohanto.android.currencyconverter.cache.datafactory

import com.zizohanto.android.currencyconverter.cache.models.SymbolCacheModel
import com.zizohanto.android.currencyconverter.data.models.HistoricalDataEntity
import java.util.*

object CacheDataFactory {

    private fun makeSymbolCacheModel(): SymbolCacheModel {
        return SymbolCacheModel(makeRandomString())
    }

    fun makeSymbolCacheModels(count: Int): List<SymbolCacheModel> {
        val symbols: MutableList<SymbolCacheModel> = mutableListOf()
        for (position in 0 until count) {
            symbols.add(makeSymbolCacheModel())
        }
        return symbols
    }

    fun makeRandomString(): String = UUID.randomUUID().toString()

    fun makeRandomStringList(count: Int): List<String> {
        val symbols: MutableList<String> = mutableListOf()
        for (position in 0 until count) {
            symbols.add(makeRandomString())
        }
        return symbols
    }

    fun makeHistoricalDataEntity(): HistoricalDataEntity {
        return HistoricalDataEntity(
            makeRandomString(),
            10L,
            makeRandomString(),
            makeRandomString(),
            10.0,
            20.0
        )
    }

}