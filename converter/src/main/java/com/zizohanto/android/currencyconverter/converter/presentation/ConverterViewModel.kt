package com.zizohanto.android.currencyconverter.converter.presentation

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zizohanto.android.currencyconverter.converter.presentation.mvi.ConverterStateMachine
import com.zizohanto.android.currencyconverter.converter.presentation.mvi.ConverterViewIntent
import com.zizohanto.android.currencyconverter.converter.presentation.mvi.ConverterViewState
import com.zizohanto.android.currencyconverter.presentation.mvi.MVIPresenter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn

/**
 * Created by zizoh on 26/November/2020.
 */

class ConverterViewModel @ViewModelInject constructor(
    private val converterStateMachine: ConverterStateMachine
) : ViewModel(), MVIPresenter<ConverterViewState, ConverterViewIntent> {

    override val viewState: Flow<ConverterViewState>
        get() = converterStateMachine.viewState

    init {
        converterStateMachine.processor.launchIn(viewModelScope)
    }

    override fun processIntent(intents: Flow<ConverterViewIntent>) {
        converterStateMachine
            .processIntents(intents)
            .launchIn(viewModelScope)
    }
}