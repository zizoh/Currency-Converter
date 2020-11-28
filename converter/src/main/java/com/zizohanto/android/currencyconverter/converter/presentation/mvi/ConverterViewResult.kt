package com.zizohanto.android.currencyconverter.converter.presentation.mvi

import com.zizohanto.android.currencyconverter.presentation.mvi.ViewResult

/**
 * Created by zizoh on 26/November/2020.
 */

sealed class ConverterViewResult : ViewResult {
    object Idle : ConverterViewResult()
    object GettingSymbols : ConverterViewResult()
    data class SymbolsLoaded(val symbols: List<String>) : ConverterViewResult()
    object GettingRates : ConverterViewResult()
    data class Converted(val convertedRate: Double) : ConverterViewResult()
    data class Error(
        val throwable: Throwable,
        val isErrorGettingSymbols: Boolean
    ) : ConverterViewResult()
}