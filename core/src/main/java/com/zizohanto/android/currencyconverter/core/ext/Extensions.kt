package com.zizohanto.android.currencyconverter.core.ext

val Throwable.errorMessage: String
    get() = message ?: localizedMessage ?: "An error occurred"