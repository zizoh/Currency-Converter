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
        val toString = getSymbols(model.rates.toString()).keys.toList()
        val id = "${toString[0]}${toString[1]}"
        return HistoricalDataEntity(id, model.timestamp, model.base, model.date, rates[0], rates[1])
    }

    private fun getRates(response: String): List<Double> {
        if (response.isEmpty()) {
            return emptyList()
        }
        val removedBraces = response.substring(1, response.lastIndex)
        val rates: MutableList<Double> = mutableListOf()
        val pairs: Array<String> = removedBraces.split(",").toTypedArray()
        for (i in pairs.indices) {
            val pair = pairs[i]
            val keyValue = pair.split("=").toTypedArray()
            rates.add(keyValue[1].trim().toDouble())
        }
        return rates
    }

    private fun getSymbols(response: String): HashMap<String, Double> {
        if (response.isEmpty()) {
            return hashMapOf()
        }
        val removedBraces = response.substring(1, response.lastIndex)
        val rates: MutableList<Double> = mutableListOf()
        val map: HashMap<String, Double> = HashMap()
        val pairs: Array<String> = removedBraces.split(",").toTypedArray()
        for (i in pairs.indices) {
            val pair = pairs[i]
            val keyValue = pair.split("=").toTypedArray()
            rates.add(keyValue[0].trim().toDouble())
            map[keyValue[0].trim()] = keyValue[1].trim().toDouble()
        }
        return map
    }
}