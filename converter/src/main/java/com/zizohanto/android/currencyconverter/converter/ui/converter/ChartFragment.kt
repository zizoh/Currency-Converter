package com.zizohanto.android.currencyconverter.converter.ui.converter

import android.graphics.Color
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IFillFormatter
import com.zizohanto.android.currencyconverter.converter.R
import com.zizohanto.android.currencyconverter.converter.databinding.FragmentChartBinding
import com.zizohanto.android.currencyconverter.converter.presentation.models.HistoricalDataModel
import com.zizohanto.android.currencyconverter.converter.presentation.views.CustomMarkerView
import com.zizohanto.android.currencyconverter.converter.presentation.views.DayAxisValueFormatter
import com.zizohanto.android.currencyconverter.core.view_binding.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.parcel.Parcelize
import javax.inject.Inject

/**
 * Created by zizoh on 03/December/2020.
 */

@AndroidEntryPoint
class ChartFragment : Fragment(R.layout.fragment_chart) {

    @Inject
    lateinit var valueFormatter: DayAxisValueFormatter

    private val binding: FragmentChartBinding by viewBinding(FragmentChartBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            val bundle = it.getParcelable<ChartFragmentBundle>(CHART_FRAGMENT_BUNDLE)
            if (bundle != null) {
                showChart(bundle.numberOfEntries)
                setData(bundle.historicalData, "USD")
            }
        }
    }

    private fun showChart(numberOfEntries: Int) {
        val chart = binding.chart

        chart.setBackgroundColor(Color.rgb(24, 93, 250))

        chart.description.isEnabled = false

        chart.setTouchEnabled(true)

        chart.isDragEnabled = true
        chart.setScaleEnabled(true)

        chart.setPinchZoom(false)

        chart.setDrawGridBackground(false)

        val x: XAxis = chart.xAxis
        x.textColor = Color.BLACK
        x.position = XAxis.XAxisPosition.BOTTOM
        x.setDrawLabels(true)
        x.setDrawGridLines(false)

        valueFormatter.setChart(chart)
        valueFormatter.setLabelCount(numberOfEntries)
        x.valueFormatter = valueFormatter

        chart.axisLeft.setDrawGridLines(false)

        chart.axisRight.isEnabled = false
        chart.axisLeft.isEnabled = false

        chart.legend.isEnabled = false
        chart.invalidate()
    }

    private fun setData(historicalData: List<HistoricalDataModel>, baseSymbol: String) {
        val values = ArrayList<Entry>()
        historicalData.forEachIndexed { index, data ->
            values.add(Entry(index.toFloat(), data.convertedRate.toFloat()))
        }
        val set: LineDataSet
        val chart = binding.chart

        // create marker to display box when values are selected
        val numberOfEntries = 30
        val markerView = CustomMarkerView(
            context, R.layout.layout_custom_marker,
            numberOfEntries, baseSymbol
        )

        markerView.chartView = chart
        chart.marker = markerView
        if (chart.data != null &&
            chart.data.dataSetCount > 0
        ) {
            set = chart.data.getDataSetByIndex(0) as LineDataSet
            set.values = values
            chart.data.notifyDataChanged()
            chart.notifyDataSetChanged()
        } else {
            // create a dataset and give it a type
            set = LineDataSet(values, "DataSet 1")
            set.mode = LineDataSet.Mode.CUBIC_BEZIER
            set.cubicIntensity = 0.2f
            set.setDrawFilled(true)
            set.setDrawCircles(false)
            set.lineWidth = 1.8f
            set.circleRadius = 4f
            set.setCircleColor(Color.WHITE)
            set.highLightColor = Color.rgb(244, 117, 117)
            set.color = Color.WHITE
            set.fillColor = Color.WHITE
            set.fillAlpha = 100
            set.setDrawHorizontalHighlightIndicator(false)
            set.fillFormatter =
                IFillFormatter { _, _ ->
                    chart.axisLeft.axisMinimum
                }

            // create a data object with the data sets
            val data = LineData(set)
            data.setValueTextSize(9f)
            data.setDrawValues(false)

            // set data
            chart.data = data
        }
    }

    companion object {
        const val CHART_FRAGMENT_BUNDLE = "CHART_FRAGMENT_BUNDLE"
        fun newInstance(
            bundle: ChartFragmentBundle
        ): ChartFragment {
            return ChartFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(CHART_FRAGMENT_BUNDLE, bundle)
                }
            }
        }
    }

    @Parcelize
    data class ChartFragmentBundle(
        val numberOfEntries: Int,
        val baseSymbol: String,
        val historicalData: List<HistoricalDataModel>
    ) : Parcelable
}