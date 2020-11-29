package com.zizohanto.android.currencyconverter.cache.models

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by zizoh on 29/November/2020.
 */

@Entity(tableName = "symbol")
data class SymbolCacheModel(@PrimaryKey val symbol: String)