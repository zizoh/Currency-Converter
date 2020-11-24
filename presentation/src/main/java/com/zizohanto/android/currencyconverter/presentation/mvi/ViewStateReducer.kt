package com.zizohanto.android.currencyconverter.presentation.mvi

interface ViewStateReducer<S : ViewState, R : ViewResult> {
    fun reduce(previous: S, result: R): S
}