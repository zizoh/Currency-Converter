package com.zizohanto.android.currencyconverter.converter.presentation.mvi

import com.zizohanto.android.currencyconverter.converter.presentation.models.ConverterDataModel
import com.zizohanto.android.currencyconverter.core.ext.errorMessage
import javax.inject.Inject

/**
 * Created by zizoh on 26/November/2020.
 */

class ConverterViewStateReducer @Inject constructor() : ConverterStateReducer {

    override fun reduce(
        previous: ConverterViewState,
        result: ConverterViewResult
    ): ConverterViewState {
        return when (result) {
            ConverterViewResult.Idle -> {
                ConverterViewState.Idle
            }
            is ConverterViewResult.SymbolsLoaded -> {
                val symbols: List<String> = result.symbols
                ConverterViewState.SymbolsLoaded(ConverterDataModel().copy(symbols = symbols))
            }
            is ConverterViewResult.Converted -> {
                when (previous) {
                    is ConverterViewState.GettingConversion -> {
                        ConverterViewState.Converted(result.convertedRate)
                    }
                    is ConverterViewState.Error -> {
                        ConverterViewState.Converted(result.convertedRate)
                    }
                    else -> {
                        ConverterViewState.Idle
                    }
                }
            }
            is ConverterViewResult.Error -> {
                when (previous) {
                    ConverterViewState.GettingSymbols -> {
                        ConverterViewState.Error(
                            result.throwable.errorMessage,
                            result.isErrorGettingSymbols
                        )
                    }
                    is ConverterViewState.SymbolsLoaded -> {
                        ConverterViewState.Error(
                            result.throwable.errorMessage,
                            result.isErrorGettingSymbols
                        )
                    }
                    is ConverterViewState.GettingConversion -> {
                        ConverterViewState.Error(
                            result.throwable.errorMessage,
                            result.isErrorGettingSymbols
                        )
                    }
                    else -> {
                        ConverterViewState.Idle
                    }
                }
            }
            ConverterViewResult.GettingSymbols -> ConverterViewState.GettingSymbols
            ConverterViewResult.GettingRates -> ConverterViewState.GettingConversion
        }
    }
}