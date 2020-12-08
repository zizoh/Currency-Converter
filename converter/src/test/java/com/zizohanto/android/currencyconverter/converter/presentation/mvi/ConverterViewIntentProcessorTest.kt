package com.zizohanto.android.currencyconverter.converter.presentation.mvi

import com.google.common.truth.Truth.assertThat
import com.zizohanto.android.currencyconverter.converter.presentation.factory.ConverterFactory.BASE
import com.zizohanto.android.currencyconverter.converter.presentation.factory.ConverterFactory.NUMBER_OF_DAYS
import com.zizohanto.android.currencyconverter.converter.presentation.factory.ConverterFactory.TARGET
import com.zizohanto.android.currencyconverter.converter.presentation.factory.ConverterFactory.makeHistoricalData
import com.zizohanto.android.currencyconverter.converter.presentation.factory.ConverterFactory.makeHistoricalDataList
import com.zizohanto.android.currencyconverter.converter.presentation.factory.ConverterFactory.makeSymbols
import com.zizohanto.android.currencyconverter.domain.repository.ConverterRepositoryImplFake
import com.zizohanto.android.currencyconverter.domain.usecase.GetConversion
import com.zizohanto.android.currencyconverter.domain.usecase.GetConversionForPeriod
import com.zizohanto.android.currencyconverter.domain.usecase.GetSymbols
import com.zizohanto.android.currencyconverter.testutils.FlowRecorder
import com.zizohanto.android.currencyconverter.testutils.TestPostExecutionThread
import com.zizohanto.android.currencyconverter.testutils.containsElements
import com.zizohanto.android.currencyconverter.testutils.recordWith
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

/**
 * Created by zizoh on 07/December/2020.
 */

class ConverterViewIntentProcessorTest {

    private val postExecutionThread = TestPostExecutionThread()
    private val repository = ConverterRepositoryImplFake()
    private val processor =
        ConverterViewIntentProcessor(
            GetSymbols(repository, postExecutionThread),
            GetConversion(repository, postExecutionThread),
            GetConversionForPeriod(repository, postExecutionThread)
        )

    private val resultRecorder: FlowRecorder<ConverterViewResult> =
        FlowRecorder(TestCoroutineScope())

    @Test
    fun `check that LoadSymbols returns symbols`() = runBlockingTest {
        processor.intentToResult(
            ConverterViewIntent.LoadSymbols
        ).recordWith(resultRecorder)

        assertThat(resultRecorder.takeAll()).containsElements(
            ConverterViewResult.GettingSymbols,
            ConverterViewResult.SymbolsLoaded(makeSymbols())
        )
    }

    @Test
    fun `check that GetRates returns rates`() = runBlockingTest {
        val amount = 419.0

        processor.intentToResult(
            ConverterViewIntent.GetRates(amount, BASE, TARGET)
        ).recordWith(resultRecorder)

        assertThat(resultRecorder.takeAll()).containsElements(
            ConverterViewResult.GettingRates,
            ConverterViewResult.Converted(makeHistoricalData())
        )
    }

    @Test
    fun `check that GetChartData returns data`() = runBlockingTest {

        processor.intentToResult(
            ConverterViewIntent.GetChartData(NUMBER_OF_DAYS, BASE, TARGET)
        ).recordWith(resultRecorder)

        assertThat(resultRecorder.takeAll()).containsElements(
            ConverterViewResult.GettingChartData,
            ConverterViewResult.ChartDataLoaded(NUMBER_OF_DAYS, makeHistoricalDataList())
        )
    }
}