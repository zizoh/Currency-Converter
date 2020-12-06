package com.zizohanto.android.currencyconverter.converter.presentation.mvi

import com.zizohanto.android.currencyconverter.converter.presentation.mappers.HistoricalDataModelMapper
import com.zizohanto.android.currencyconverter.converter.presentation.models.ConverterDataModel
import com.zizohanto.android.currencyconverter.core.ext.errorMessage
import javax.inject.Inject

/**
 * Created by zizoh on 26/November/2020.
 */

class ConverterViewStateReducer @Inject constructor(
    private val mapper: HistoricalDataModelMapper
) : ConverterStateReducer {

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
                val historicalData = mapper.mapToModel(result.historicalData)
                ConverterViewState.Converted(historicalData)
            }
            is ConverterViewResult.Error -> {
                when (result) {
                    is ConverterViewResult.Error.ErrorGettingSymbols -> {
                        ConverterViewState.Error.ErrorGettingSymbols(result.throwable.errorMessage)
                    }
                    is ConverterViewResult.Error.ErrorGettingConversion -> {
                        ConverterViewState.Error.ErrorGettingConversion(result.throwable.errorMessage)
                    }
                    is ConverterViewResult.Error.ErrorGettingChart -> {
                        ConverterViewState.Error.ErrorGettingChart(result.throwable.errorMessage)
                    }
                }
            }
            ConverterViewResult.GettingSymbols -> ConverterViewState.GettingSymbols
            ConverterViewResult.GettingRates -> ConverterViewState.GettingConversion
            ConverterViewResult.GettingChartData -> ConverterViewState.GettingChartData
            is ConverterViewResult.ChartDataLoaded -> {
                val historicalData = mapper.mapToModelList(result.historicalData)
                ConverterViewState.ChartDataLoaded(result.numberOfEntries, historicalData)
            }
        }
    }
}