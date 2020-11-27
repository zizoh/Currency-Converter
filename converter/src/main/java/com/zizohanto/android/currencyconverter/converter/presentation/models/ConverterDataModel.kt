package com.zizohanto.android.currencyconverter.converter.presentation.models

/**
 * Created by zizoh on 26/November/2020.
 */

data class ConverterDataModel(
    val amount: Double = 0.0,
    val convertedRate: Double = 0.0,
    val symbols: List<String> = emptyList(),
    val baseRateSymbol: String = "USD",
    val targetRateSymbol: String = "NGN"
)