package com.zizohanto.android.currencyconverter.presentation.mvi

import kotlinx.coroutines.flow.Flow

interface IntentProcessor<in I : ViewIntent, out R : ViewResult> {
    fun intentToResult(viewIntent: I): Flow<R>
}