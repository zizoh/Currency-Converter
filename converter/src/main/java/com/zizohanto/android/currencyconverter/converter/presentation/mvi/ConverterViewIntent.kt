package com.zizohanto.android.currencyconverter.converter.presentation.mvi

import com.zizohanto.android.currencyconverter.presentation.mvi.ViewIntent

/**
 * Created by zizoh on 26/November/2020.
 */

sealed class ConverterViewIntent : ViewIntent {
    object Idle : ConverterViewIntent()
    object LoadSymbols : ConverterViewIntent()
    data class GetRates(
        val amount: Double,
        val base: String,
        val target: String
    ) : ConverterViewIntent()

    data class GetChartData(
        val numberOfDays: Int,
        val base: String,
        val target: String
    ) : ConverterViewIntent()
}