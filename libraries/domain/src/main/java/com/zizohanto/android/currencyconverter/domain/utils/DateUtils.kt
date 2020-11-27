package com.zizohanto.android.currencyconverter.domain.utils

import org.joda.time.DateTime

object DateUtils {
    fun getCurrentDate(): String {
        return DateTime().toString("yyyy-MM-dd")
    }
}