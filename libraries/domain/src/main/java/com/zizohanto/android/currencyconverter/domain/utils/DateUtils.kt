package com.zizohanto.android.currencyconverter.domain.utils

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormatterBuilder

object DateUtils {
    fun getCurrentDate(): String {
        return DateTime().toString("yyyy-MM-dd")
    }

    fun getDatesWithinPeriod(numberOfDays: Int): List<String> {
        val dates: ArrayList<String> = ArrayList()
        val intProgression: IntProgression = numberOfDays downTo 1
        intProgression.forEachIndexed { index, _ ->
            val element = DateTime().minusDays(index).toString("yyyy-MM-dd")
            dates.add(element)
        }
        return dates
    }

    fun getLabelForPosition(position: Int, labelCount: Int): String {
        if (position == 0) {
            return ""
        }
        val fmt = DateTimeFormatterBuilder()
            .appendDayOfMonth(2)
            .appendLiteral(' ')
            .appendMonthOfYearShortText()
            .toFormatter()
        return fmt.print(DateTime().minusDays(labelCount - position))
    }
}


