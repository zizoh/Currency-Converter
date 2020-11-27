package com.zizohanto.android.currencyconverter.remote.mapper

import com.zizohanto.android.currencyconverter.data.models.SymbolEntity
import com.zizohanto.android.currencyconverter.remote.mapper.base.RemoteModelMapper
import com.zizohanto.android.currencyconverter.remote.model.SymbolRemoteModel
import javax.inject.Inject

/**
 * Created by zizoh on 25/November/2020.
 */

class SymbolRemoteModelMapper @Inject constructor() :
    RemoteModelMapper<SymbolRemoteModel, SymbolEntity> {

    override fun mapFromModel(model: SymbolRemoteModel): SymbolEntity {
        val symbols = getSymbols(model.symbols.toString())
        return SymbolEntity(symbols)
    }

    fun getSymbols(symbols: String): List<String> {
        if (symbols.isEmpty()) {
            return emptyList()
        }
        val removedBraces = symbols.substring(1, symbols.lastIndex)
        val map: HashMap<String, String> = HashMap()
        val pairs = removedBraces.split(",").toTypedArray()
        for (i in pairs.indices) {
            val pair = pairs[i]
            val keyValue = pair.split("=").toTypedArray()
            map[keyValue[0]] = keyValue[1]
        }
        return map.keys.toList()
    }
}