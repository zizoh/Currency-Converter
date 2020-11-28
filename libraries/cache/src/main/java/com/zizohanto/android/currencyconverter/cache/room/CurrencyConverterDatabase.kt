package com.zizohanto.android.currencyconverter.cache.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.zizohanto.android.currencyconverter.cache.BuildConfig
import com.zizohanto.android.currencyconverter.cache.models.HistoricalDataCacheModel

/**
 * Created by zizoh on 28/November/2020.
 */

@Database(
    entities = [
        HistoricalDataCacheModel::class
    ],
    version = BuildConfig.databaseVersion,
    exportSchema = false
)
abstract class CurrencyConverterDatabase : RoomDatabase() {

    abstract val historicalDataDao: HistoricalDataDao

    companion object {
        private const val DATABASE_NAME: String = "shopping_list_db"
        fun build(context: Context): CurrencyConverterDatabase = Room.databaseBuilder(
            context.applicationContext,
            CurrencyConverterDatabase::class.java, DATABASE_NAME
        ).fallbackToDestructiveMigration().build()
    }
}