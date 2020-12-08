package com.zizohanto.android.currencyconverter.converter.presentation

import com.google.common.truth.Truth.assertThat
import com.zizohanto.android.currencyconverter.converter.presentation.factory.ConverterFactory.BASE
import com.zizohanto.android.currencyconverter.converter.presentation.factory.ConverterFactory.NUMBER_OF_DAYS
import com.zizohanto.android.currencyconverter.converter.presentation.factory.ConverterFactory.TARGET
import com.zizohanto.android.currencyconverter.converter.presentation.factory.ConverterFactory.makeConverterDataModel
import com.zizohanto.android.currencyconverter.converter.presentation.factory.ConverterFactory.makeHistoricalData
import com.zizohanto.android.currencyconverter.converter.presentation.factory.ConverterFactory.makeHistoricalDataList
import com.zizohanto.android.currencyconverter.converter.presentation.mappers.HistoricalDataModelMapper
import com.zizohanto.android.currencyconverter.converter.presentation.mvi.*
import com.zizohanto.android.currencyconverter.domain.repository.ConverterRepositoryImplFake
import com.zizohanto.android.currencyconverter.domain.usecase.GetConversion
import com.zizohanto.android.currencyconverter.domain.usecase.GetConversionForPeriod
import com.zizohanto.android.currencyconverter.domain.usecase.GetSymbols
import com.zizohanto.android.currencyconverter.testutils.*
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestCoroutineScope
import org.junit.Rule
import org.junit.Test

/**
 * Created by zizoh on 07/December/2020.
 */

class ConverterViewModelTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private val stateRecorder: FlowRecorder<ConverterViewState> = FlowRecorder(TestCoroutineScope())

    private val repository = ConverterRepositoryImplFake()

    private val postExecutionThread = TestPostExecutionThread()

    private val mapper = HistoricalDataModelMapper()

    private val viewModel: ConverterViewModel by lazy {
        ConverterViewModel(
            ConverterViewStateMachine(
                ConverterViewIntentProcessor(
                    GetSymbols(
                        repository,
                        postExecutionThread
                    ),
                    GetConversion(repository, postExecutionThread),
                    GetConversionForPeriod(repository, postExecutionThread)
                ),
                ConverterViewStateReducer(mapper)
            )
        )
    }

    @Test
    fun `check that idle is first emitted`() {
        /**
         * Pause the dispatcher so that coroutines don't run yet.
         * This allows us capture the initial viewState emitted from [ConverterViewModel.viewState].
         * That emission usually gets lost before we subscribe to the stream.
         */
        mainCoroutineRule.pauseDispatcher()
        viewModel.viewState.recordWith(stateRecorder)
        // Resume the dispatcher and then run the coroutines
        mainCoroutineRule.resumeDispatcher()
        assertThat(stateRecorder.take(1))
            .containsElements(ConverterViewState.Idle)
    }

    @Test
    fun `check that loaded symbols viewState is emitted`() {
        mainCoroutineRule.pauseDispatcher()
        viewModel.viewState.recordWith(stateRecorder)
        // Resume the dispatcher and then run the coroutines
        mainCoroutineRule.resumeDispatcher()
        assertThat(stateRecorder.takeAll())
            .containsElements(
                ConverterViewState.Idle,
                ConverterViewState.GettingSymbols,
                ConverterViewState.SymbolsLoaded(makeConverterDataModel())
            )
    }

    @Test
    fun `check that converted viewState is emitted`() {
        mainCoroutineRule.pauseDispatcher()
        viewModel.viewState.recordWith(stateRecorder)
        mainCoroutineRule.resumeDispatcher()

        viewModel.processIntent(flowOf(ConverterViewIntent.GetRates(1759.0, BASE, TARGET)))

        assertThat(stateRecorder.takeAll())
            .containsElements(
                ConverterViewState.Idle,
                ConverterViewState.GettingSymbols,
                ConverterViewState.SymbolsLoaded(makeConverterDataModel()),
                ConverterViewState.GettingConversion,
                ConverterViewState.Converted(mapper.mapToModel(makeHistoricalData()))
            )
    }

    @Test
    fun `check that chart loaaded viewState is emitted`() {
        mainCoroutineRule.pauseDispatcher()
        viewModel.viewState.recordWith(stateRecorder)
        mainCoroutineRule.resumeDispatcher()

        viewModel.processIntent(flowOf(ConverterViewIntent.GetRates(1759.0, BASE, TARGET)))
        viewModel.processIntent(flowOf(ConverterViewIntent.GetChartData(NUMBER_OF_DAYS, BASE, TARGET)))

        assertThat(stateRecorder.takeAll())
            .containsElements(
                ConverterViewState.Idle,
                ConverterViewState.GettingSymbols,
                ConverterViewState.SymbolsLoaded(makeConverterDataModel()),
                ConverterViewState.GettingConversion,
                ConverterViewState.Converted(mapper.mapToModel(makeHistoricalData())),
                ConverterViewState.GettingChartData,
                ConverterViewState.ChartDataLoaded(NUMBER_OF_DAYS, mapper.mapToModelList(makeHistoricalDataList()))
            )
    }
}