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
            ConverterViewResult.Idle -> ConverterViewState.Idle
            is ConverterViewResult.SymbolsLoaded -> {
                val symbols: List<String> = result.symbols
                ConverterViewState.SymbolsLoaded(ConverterDataModel().copy(symbols = symbols))
            }
            is ConverterViewResult.Converted -> {
                when (previous) {
                    is ConverterViewState.GettingRates -> {
                        val state = previous.state.copy(
                            convertedRate = result.convertedRate,
                        )
                        ConverterViewState.Converted(state)
                    }
                    is ConverterViewState.Error -> {
                        val state = previous.state.copy(
                            convertedRate = result.convertedRate
                        )
                        ConverterViewState.Converted(state)
                    }
                    else -> ConverterViewState.Idle
                }
            }
            is ConverterViewResult.Error -> {
                when (previous) {
                    ConverterViewState.GettingSymbols -> {
                        ConverterViewState.Error(
                            result.throwable.errorMessage,
                            result.isErrorGettingSymbols,
                            ConverterDataModel(),
                        )
                    }
                    is ConverterViewState.GettingRates -> {
                        ConverterViewState.Error(
                            result.throwable.errorMessage,
                            result.isErrorGettingSymbols,
                            previous.state
                        )
                    }
                    else -> ConverterViewState.Idle
                }
            }
        }
    }
}