package com.zizohanto.android.currencyconverter.remote.mapper

import com.zizohanto.android.currencyconverter.data.models.HistoricalDataEntity
import com.zizohanto.android.currencyconverter.remote.mapper.base.RemoteModelMapper
import com.zizohanto.android.currencyconverter.remote.model.HistoricalDataRemoteModel
import javax.inject.Inject

/**
 * Created by zizoh on 25/November/2020.
 */

class HistoricalDataRemoteModelMapper @Inject constructor() :
    RemoteModelMapper<HistoricalDataRemoteModel, HistoricalDataEntity> {

    override fun mapFromModel(model: HistoricalDataRemoteModel): HistoricalDataEntity {
        val rates = getRates(model.rates.toString())
        return HistoricalDataEntity(model.timestamp, model.base, model.date, rates[0], rates[1])
    }

    private fun getRates(symbols: String): List<Double> {
        if (symbols.isEmpty()) {
            return emptyList()
        }
        val removedBraces = symbols.substring(1, symbols.lastIndex)
        val rates: MutableList<Double> = mutableListOf()
        val pairs: Array<String> = removedBraces.split(",").toTypedArray()
        for (i in pairs.indices) {
            val pair = pairs[i]
            val keyValue = pair.split("=").toTypedArray()
            rates.add(keyValue[1].trim().toDouble())
        }
        return rates
    }
}