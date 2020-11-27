package com.zizohanto.android.currencyconverter.converter.presentation.mvi

import com.zizohanto.android.currencyconverter.converter.presentation.models.ConverterDataModel
import com.zizohanto.android.currencyconverter.presentation.mvi.ViewState

/**
 * Created by zizoh on 26/November/2020.
 */

sealed class ConverterViewState : ViewState {
    object Idle : ConverterViewState()
    object GettingSymbols : ConverterViewState()
    data class SymbolsLoaded(val state: ConverterDataModel) : ConverterViewState()
    data class GettingRates(val state: ConverterDataModel) : ConverterViewState()
    data class Converted(val state: ConverterDataModel) : ConverterViewState()
    data class Error(
        val message: String,
        val isErrorGettingSymbols: Boolean,
        val state: ConverterDataModel
    ) : ConverterViewState()

}