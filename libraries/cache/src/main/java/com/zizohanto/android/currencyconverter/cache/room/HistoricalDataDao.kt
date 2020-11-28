package com.zizohanto.android.currencyconverter.cache.room

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.zizohanto.android.currencyconverter.cache.models.HistoricalDataCacheModel

/**
 * Created by zizoh on 28/November/2020.
 */

interface HistoricalDataDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistoricalData(data: HistoricalDataCacheModel)

    @Query("SELECT * FROM historical_data WHERE id=:id")
    suspend fun getHistoricalDataWithId(id: String): HistoricalDataCacheModel?
}