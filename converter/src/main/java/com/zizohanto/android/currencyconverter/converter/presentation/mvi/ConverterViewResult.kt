package com.zizohanto.android.currencyconverter.converter.presentation.mvi

import com.zizohanto.android.currencyconverter.domain.models.HistoricalData
import com.zizohanto.android.currencyconverter.presentation.mvi.ViewResult

/**
 * Created by zizoh on 26/November/2020.
 */

sealed class ConverterViewResult : ViewResult {
    object Idle : ConverterViewResult()
    object GettingSymbols : ConverterViewResult()
    data class SymbolsLoaded(val symbols: List<String>) : ConverterViewResult()
    object GettingRates : ConverterViewResult()
    data class Converted(val historicalData: HistoricalData) : ConverterViewResult()
    data class Error(
        val throwable: Throwable,
        val isErrorGettingSymbols: Boolean
    ) : ConverterViewResult()

    object GettingChartData : ConverterViewResult()
    data class ChartDataLoaded(val historicalData: List<HistoricalData>) : ConverterViewResult()
}