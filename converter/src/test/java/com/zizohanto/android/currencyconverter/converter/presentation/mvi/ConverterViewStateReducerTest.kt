package com.zizohanto.android.currencyconverter.converter.presentation.mvi

import com.google.common.truth.Truth.assertThat
import com.zizohanto.android.currencyconverter.converter.presentation.factory.ConverterFactory.NUMBER_OF_DAYS
import com.zizohanto.android.currencyconverter.converter.presentation.factory.ConverterFactory.makeConverterDataModel
import com.zizohanto.android.currencyconverter.converter.presentation.factory.ConverterFactory.makeHistoricalData
import com.zizohanto.android.currencyconverter.converter.presentation.factory.ConverterFactory.makeHistoricalDataList
import com.zizohanto.android.currencyconverter.converter.presentation.mappers.HistoricalDataModelMapper
import com.zizohanto.android.currencyconverter.converter.presentation.mvi.ConverterViewState.Idle
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

/**
 * Created by zizoh on 07/December/2020.
 */

class ConverterViewStateReducerTest {
    private val mapper = HistoricalDataModelMapper()
    private val reducer = ConverterViewStateReducer(mapper)

    @Test
    fun `GettingSymbols is emitted when ConverterViewResult is GettingSymbols`() {
        runBlockingTest {
            val initialState: ConverterViewState = Idle
            val viewState: ConverterViewState = reducer.reduce(
                initialState,
                ConverterViewResult.GettingSymbols
            )
            assertThat(viewState).isEqualTo(ConverterViewState.GettingSymbols)
        }
    }

    @Test
    fun `SymbolsLoaded is emitted when ConverterViewResult is SymbolsLoaded`() {
        runBlockingTest {
            val initialState: ConverterViewState = Idle
            val viewState: ConverterViewState = reducer.reduce(
                initialState,
                ConverterViewResult.SymbolsLoaded(listOf("USD", "NGN"))
            )
            assertThat(viewState).isEqualTo(ConverterViewState.SymbolsLoaded(makeConverterDataModel()))
        }
    }

    @Test
    fun `GettingConversion is emitted when ConverterViewResult is GettingRates`() {
        runBlockingTest {
            val initialState: ConverterViewState = Idle
            val viewState: ConverterViewState = reducer.reduce(
                initialState,
                ConverterViewResult.GettingRates
            )
            assertThat(viewState).isEqualTo(ConverterViewState.GettingConversion)
        }
    }

    @Test
    fun `Converted is emitted when ConverterViewResult is Converted`() {
        runBlockingTest {
            val initialState: ConverterViewState = Idle
            val historicalData = makeHistoricalData()
            val viewState: ConverterViewState = reducer.reduce(
                initialState,
                ConverterViewResult.Converted(historicalData)
            )
            assertThat(viewState).isEqualTo(
                ConverterViewState.Converted(
                    mapper.mapToModel(
                        historicalData
                    )
                )
            )
        }
    }

    @Test
    fun `GettingChartData is emitted when ConverterViewResult is GettingChartData`() {
        runBlockingTest {
            val initialState: ConverterViewState = Idle
            val viewState: ConverterViewState = reducer.reduce(
                initialState,
                ConverterViewResult.GettingChartData
            )
            assertThat(viewState).isEqualTo(ConverterViewState.GettingChartData)
        }
    }

    @Test
    fun `ChartDataLoaded is emitted when ConverterViewResult is ChartDataLoaded`() {
        runBlockingTest {
            val initialState: ConverterViewState = Idle
            val historicalData = makeHistoricalDataList()
            val viewState: ConverterViewState = reducer.reduce(
                initialState,
                ConverterViewResult.ChartDataLoaded(NUMBER_OF_DAYS, historicalData)
            )
            assertThat(viewState).isEqualTo(
                ConverterViewState.ChartDataLoaded(
                    NUMBER_OF_DAYS,
                    mapper.mapToModelList(historicalData)
                )
            )
        }
    }

}