package com.zizohanto.android.currencyconverter.cache.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.zizohanto.android.currencyconverter.cache.models.SymbolCacheModel

/**
 * Created by zizoh on 28/November/2020.
 */

@Dao
interface SymbolDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSymbols(symbols: List<SymbolCacheModel>)

    @Query("SELECT * FROM symbol")
    suspend fun getSymbols(): List<SymbolCacheModel>
}