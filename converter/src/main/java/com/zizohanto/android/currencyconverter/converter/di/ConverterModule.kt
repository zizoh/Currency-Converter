package com.zizohanto.android.currencyconverter.converter.di

import com.zizohanto.android.currencyconverter.converter.presentation.mvi.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

/**
 * Created by zizoh on 26/November/2020.
 */

@InstallIn(ActivityRetainedComponent::class)
@Module
interface ConverterModule {

    @get:Binds
    val ConverterViewIntentProcessor.intentProcessor: ConverterIntentProcessor

    @get:Binds
    val ConverterViewStateReducer.reducer: ConverterStateReducer

    @get:Binds
    val ConverterViewStateMachine.stateMachine: ConverterStateMachine
}