package com.zizohanto.android.currencyconverter.converter.presentation.mvi

import com.zizohanto.android.currencyconverter.presentation.mvi.IntentProcessor
import com.zizohanto.android.currencyconverter.presentation.mvi.StateMachine
import com.zizohanto.android.currencyconverter.presentation.mvi.ViewStateReducer

typealias ConverterIntentProcessor =
        @JvmSuppressWildcards IntentProcessor<ConverterViewIntent, ConverterViewResult>

typealias ConverterStateReducer =
        @JvmSuppressWildcards ViewStateReducer<ConverterViewState, ConverterViewResult>

typealias ConverterStateMachine =
        @JvmSuppressWildcards StateMachine<ConverterViewIntent, ConverterViewState, ConverterViewResult>