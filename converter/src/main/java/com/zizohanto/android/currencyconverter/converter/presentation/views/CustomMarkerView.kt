package com.zizohanto.android.currencyconverter.converter.presentation.views

import android.annotation.SuppressLint
import android.content.Context
import android.widget.TextView
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.CandleEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import com.zizohanto.android.currencyconverter.converter.R
import com.zizohanto.android.currencyconverter.domain.utils.DateUtils

/**
 * Created by zizoh on 02/December/2020.
 */

@SuppressLint("ViewConstructor")
class CustomMarkerView(
    context: Context?, layoutResource: Int,
    private val xLabelCount: Int,
    private val baseSymbol: String
) : MarkerView(context, layoutResource) {

    private val tvDate: TextView = findViewById(R.id.tv_date)
    private val tvRate: TextView = findViewById(R.id.tv_rate)

    override fun refreshContent(e: Entry, highlight: Highlight?) {
        if (e is CandleEntry) {
            tvDate.text = DateUtils.getLabelForPosition(e.x.toInt(), xLabelCount)
            val convertedRate = e.y.toInt().toString()
            tvRate.text = "1 $baseSymbol = $convertedRate"
        } else {
            tvDate.text = DateUtils.getLabelForPosition(e.x.toInt(), xLabelCount)
            val convertedRate = e.y.toInt().toString()
            tvRate.text = "1 $baseSymbol = $convertedRate"
        }
        super.refreshContent(e, highlight)
    }

    override fun getOffset(): MPPointF {
        return MPPointF((-(width / 2)).toFloat(), (-height).toFloat())
    }

}