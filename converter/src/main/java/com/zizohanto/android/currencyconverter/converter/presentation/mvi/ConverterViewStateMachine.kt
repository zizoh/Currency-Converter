package com.zizohanto.android.currencyconverter.converter.presentation.mvi

import javax.inject.Inject

/**
 * Created by zizoh on 26/November/2020.
 */

class ConverterViewStateMachine @Inject constructor(
    intentProcessor: ConverterIntentProcessor,
    reducer: ConverterStateReducer
) : ConverterStateMachine(
    intentProcessor,
    reducer,
    ConverterViewIntent.GetChartData(30, "USD", "NGN"),
    ConverterViewState.Idle
)