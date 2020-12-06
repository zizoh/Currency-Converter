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
    sealed class Error : ConverterViewResult() {
        data class ErrorGettingSymbols(val throwable: Throwable) : Error()
        data class ErrorGettingConversion(val throwable: Throwable) : Error()
        data class ErrorGettingChart(val throwable: Throwable) : Error()
    }

    object GettingChartData : ConverterViewResult()
    data class ChartDataLoaded(
        val numberOfEntries: Int,
        val historicalData: List<HistoricalData>
    ) : ConverterViewResult()
}