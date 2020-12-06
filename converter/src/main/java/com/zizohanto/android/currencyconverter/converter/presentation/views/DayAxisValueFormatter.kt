package com.zizohanto.android.currencyconverter.converter.presentation.views

import com.github.mikephil.charting.charts.BarLineChartBase
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.ValueFormatter
import com.zizohanto.android.currencyconverter.domain.utils.DateUtils
import javax.inject.Inject

/**
 * Created by zizoh on 01/December/2020.
 */


class DayAxisValueFormatter @Inject constructor() : ValueFormatter() {
    private var labelCount: Int = 0
    private lateinit var chart: BarLineChartBase<*>

    fun setChart(chart: BarLineChartBase<*>) {
        this.chart = chart
    }

    fun setLabelCount(xLabelCount: Int) {
        labelCount = xLabelCount
    }

    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
        return DateUtils.getLabelForPosition(value.toInt(), labelCount)
    }

}