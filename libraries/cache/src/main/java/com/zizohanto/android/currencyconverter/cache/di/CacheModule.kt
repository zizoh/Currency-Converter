package com.zizohanto.android.currencyconverter.cache.di

import android.content.Context
import com.zizohanto.android.currencyconverter.cache.impl.ConverterCacheImpl
import com.zizohanto.android.currencyconverter.cache.room.CurrencyConverterDatabase
import com.zizohanto.android.currencyconverter.cache.room.HistoricalDataDao
import com.zizohanto.android.currencyconverter.data.contract.cache.ConverterCache
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Created by zizoh on 28/November/2020.
 */

@InstallIn(SingletonComponent::class)
@Module
interface CacheModule {

    @get:Binds
    val ConverterCacheImpl.converterCache: ConverterCache

    companion object {
        @[Provides Singleton]
        fun provideDatabase(@ApplicationContext context: Context): CurrencyConverterDatabase {
            return CurrencyConverterDatabase.build(context)
        }

        @[Provides Singleton]
        fun provideHistoricalDataDao(database: CurrencyConverterDatabase): HistoricalDataDao {
            return database.historicalDataDao
        }
    }
}