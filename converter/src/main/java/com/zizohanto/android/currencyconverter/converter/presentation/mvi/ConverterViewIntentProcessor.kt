package com.zizohanto.android.currencyconverter.converter.presentation.mvi

import com.zizohanto.android.currencyconverter.domain.usecase.GetRates
import com.zizohanto.android.currencyconverter.domain.usecase.GetSymbols
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Created by zizoh on 26/November/2020.
 */

class ConverterViewIntentProcessor @Inject constructor(
    private val getSymbols: GetSymbols,
    private val getRates: GetRates
) : ConverterIntentProcessor {
    override fun intentToResult(viewIntent: ConverterViewIntent): Flow<ConverterViewResult> {
        return when (viewIntent) {
            ConverterViewIntent.Idle -> flowOf(ConverterViewResult.Idle)
            ConverterViewIntent.LoadSymbols -> loadSymbols()
            is ConverterViewIntent.GetRates -> loadRates(
                viewIntent.amount,
                viewIntent.base,
                viewIntent.target
            )
        }
    }

    private fun loadSymbols(): Flow<ConverterViewResult> {
        return getSymbols()
            .map<List<String>, ConverterViewResult> {
                ConverterViewResult.SymbolsLoaded(it)
            }.catch { error ->
                error.printStackTrace()
                emit(ConverterViewResult.Error(error, isErrorGettingSymbols = true))
            }
    }


    private fun loadRates(amount: Double, base: String, target: String): Flow<ConverterViewResult> {
        val params = GetRates.Params(amount, base, target)
        return getRates(params)
            .map<Double, ConverterViewResult> {
                ConverterViewResult.Converted(it)
            }.catch { error ->
                error.printStackTrace()
                emit(ConverterViewResult.Error(error, isErrorGettingSymbols = false))
            }
    }

}