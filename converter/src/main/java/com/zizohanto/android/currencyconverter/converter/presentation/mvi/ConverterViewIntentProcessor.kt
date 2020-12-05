package com.zizohanto.android.currencyconverter.converter.presentation.mvi

import com.zizohanto.android.currencyconverter.domain.models.HistoricalData
import com.zizohanto.android.currencyconverter.domain.usecase.GetConversion
import com.zizohanto.android.currencyconverter.domain.usecase.GetConversionForPeriod
import com.zizohanto.android.currencyconverter.domain.usecase.GetSymbols
import kotlinx.coroutines.flow.*
import javax.inject.Inject

/**
 * Created by zizoh on 26/November/2020.
 */

class ConverterViewIntentProcessor @Inject constructor(
    private val getSymbols: GetSymbols,
    private val getConversion: GetConversion,
    private val getChartData: GetConversionForPeriod
) : ConverterIntentProcessor {
    override fun intentToResult(viewIntent: ConverterViewIntent): Flow<ConverterViewResult> {
        return when (viewIntent) {
            ConverterViewIntent.Idle -> flowOf(ConverterViewResult.Idle)
            ConverterViewIntent.LoadSymbols -> loadSymbols()
            is ConverterViewIntent.GetRates -> getConversion(
                viewIntent.amount,
                viewIntent.base,
                viewIntent.target
            )
            is ConverterViewIntent.GetChartData -> getChartData(
                viewIntent.numberOfDays,
                viewIntent.base,
                viewIntent.target
            )
        }
    }

    private fun loadSymbols(): Flow<ConverterViewResult> {
        return getSymbols()
            .map<List<String>, ConverterViewResult> {
                ConverterViewResult.SymbolsLoaded(it)
            }.onStart {
                emit(ConverterViewResult.GettingSymbols)
            }
            .catch { error ->
                error.printStackTrace()
                emit(ConverterViewResult.Error(error, isErrorGettingSymbols = true))
            }
    }


    private fun getConversion(
        amount: Double,
        base: String,
        target: String
    ): Flow<ConverterViewResult> {
        val params = GetConversion.Params(amount, base, target)
        return getConversion(params)
            .map<HistoricalData, ConverterViewResult> {
                ConverterViewResult.Converted(it)
            }.onStart {
                emit(ConverterViewResult.GettingRates)
            }.catch { error ->
                error.printStackTrace()
                emit(ConverterViewResult.Error(error, isErrorGettingSymbols = false))
            }
    }

    private fun getChartData(
        numberOfDays: Int,
        base: String,
        target: String
    ): Flow<ConverterViewResult> {
        val params = GetConversionForPeriod.Params(numberOfDays, base, target)
        return getChartData(params)
            .map<List<HistoricalData>, ConverterViewResult> {
                ConverterViewResult.ChartDataLoaded(numberOfDays, it)
            }.onStart {
                emit(ConverterViewResult.GettingChartData)
            }.catch { error ->
                error.printStackTrace()
                // todo remove check for error type
                emit(ConverterViewResult.Error(error, isErrorGettingSymbols = false))
            }
    }

}