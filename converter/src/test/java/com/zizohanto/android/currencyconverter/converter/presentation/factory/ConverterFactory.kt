package com.zizohanto.android.currencyconverter.converter.presentation.factory

import com.zizohanto.android.currencyconverter.converter.presentation.models.ConverterDataModel
import com.zizohanto.android.currencyconverter.domain.models.HistoricalData

/**
 * Created by zizoh on 07/December/2020.
 */

object ConverterFactory {

    fun makeHistoricalData(): HistoricalData {
        return HistoricalData(CONVERTED_RATE, TIME_STAMP)
    }

    fun makeHistoricalDataList(): List<HistoricalData> {
        val mutableList: MutableList<HistoricalData> = mutableListOf()
        val historicalData = listOf(
            HistoricalData(convertedRate = 377.3925723467343, timeStamp = 1606888517),
            HistoricalData(convertedRate = 380.9997042959095, timeStamp = 1606867199),
            HistoricalData(convertedRate = 380.50055621638825, timeStamp = 1606780799),
            HistoricalData(convertedRate = 380.5042611168286, timeStamp = 1606694399),
            HistoricalData(convertedRate = 380.5037258384704, timeStamp = 1606607999),
            HistoricalData(convertedRate = 380.50372649001093, timeStamp = 1606521599),
            HistoricalData(convertedRate = 381.190521633304, timeStamp = 1606435199),
            HistoricalData(convertedRate = 381.000231572765, timeStamp = 1606348799),
            HistoricalData(convertedRate = 381.512731279162, timeStamp = 1606262399),
            HistoricalData(convertedRate = 380.49799007536376, timeStamp = 1606175999),
            HistoricalData(convertedRate = 380.99978341461605, timeStamp = 1606089599),
            HistoricalData(convertedRate = 381.00034411068896, timeStamp = 1606003199),
            HistoricalData(convertedRate = 381.00034411068896, timeStamp = 1605916799),
            HistoricalData(convertedRate = 381.4987944679521, timeStamp = 1605830399),
            HistoricalData(convertedRate = 381.00022790022024, timeStamp = 1605743999),
            HistoricalData(convertedRate = 381.4982260431532, timeStamp = 1605657599),
            HistoricalData(convertedRate = 380.50259252607066, timeStamp = 1605571199),
            HistoricalData(convertedRate = 380.49368834710083, timeStamp = 1605484799),
            HistoricalData(convertedRate = 380.5037024519661, timeStamp = 1605398399),
            HistoricalData(convertedRate = 380.5037024519661, timeStamp = 1605311999),
            HistoricalData(convertedRate = 379.4996458737515, timeStamp = 1605225599),
            HistoricalData(convertedRate = 381.1998090064089, timeStamp = 1605139199),
            HistoricalData(convertedRate = 379.99984090972555, timeStamp = 1605052799),
            HistoricalData(convertedRate = 380.0001031880891, timeStamp = 1604966399),
            HistoricalData(convertedRate = 381.9997275614479, timeStamp = 1604879999),
            HistoricalData(convertedRate = 382.0003435977548, timeStamp = 1604793599),
            HistoricalData(convertedRate = 382.00032591943807, timeStamp = 1604707199),
            HistoricalData(convertedRate = 383.00019720543816, timeStamp = 1604620799),
            HistoricalData(convertedRate = 382.9999198823805, timeStamp = 1604534399),
            HistoricalData(convertedRate = 383.0002968951909, timeStamp = 1604447999)
        )
        mutableList.addAll(historicalData)
        return mutableList
    }

    fun makeSymbols(): List<String> {
        return listOf(BASE, TARGET)
    }

    fun makeConverterDataModel():ConverterDataModel = ConverterDataModel()

    const val BASE = "USD"
    const val TARGET = "NGN"
    const val NUMBER_OF_DAYS = 30
    private const val CONVERTED_RATE = 1759.0

    private const val TIME_STAMP: Long = 1606755566
}