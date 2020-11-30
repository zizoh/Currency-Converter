package com.zizohanto.android.currencyconverter.cache.mappers

import com.zizohanto.android.currencyconverter.cache.mappers.base.CacheModelMapper
import com.zizohanto.android.currencyconverter.cache.models.SymbolCacheModel
import javax.inject.Inject

/**
 * Created by zizoh on 29/November/2020.
 */

class SymbolCacheModelMapper @Inject constructor() :
    CacheModelMapper<SymbolCacheModel, String> {

    override fun mapToModel(entity: String): SymbolCacheModel {
        return SymbolCacheModel(entity)
    }

    override fun mapToEntity(model: SymbolCacheModel): String {
        return model.symbol
    }
}