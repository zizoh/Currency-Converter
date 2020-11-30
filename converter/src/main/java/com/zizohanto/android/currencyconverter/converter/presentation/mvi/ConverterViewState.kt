package com.zizohanto.android.currencyconverter.converter.presentation.mvi

import com.zizohanto.android.currencyconverter.converter.presentation.models.ConverterDataModel
import com.zizohanto.android.currencyconverter.converter.presentation.models.HistoricalDataModel
import com.zizohanto.android.currencyconverter.presentation.mvi.ViewState

/**
 * Created by zizoh on 26/November/2020.
 */

sealed class ConverterViewState : ViewState {
    object Idle : ConverterViewState()
    object GettingSymbols : ConverterViewState()
    data class SymbolsLoaded(val state: ConverterDataModel) : ConverterViewState()
    object GettingConversion : ConverterViewState()
    data class Converted(val historicalData: HistoricalDataModel) : ConverterViewState()
    data class Error(
        val message: String,
        val isErrorGettingSymbols: Boolean
    ) : ConverterViewState()

}