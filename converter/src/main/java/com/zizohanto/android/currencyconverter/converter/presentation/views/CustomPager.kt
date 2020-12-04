package com.zizohanto.android.currencyconverter.converter.presentation.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.viewpager.widget.ViewPager


/**
 * Created by zizoh on 04/December/2020.
 */

class CustomPager constructor(context: Context, attrs: AttributeSet?) : ViewPager(context, attrs) {
    private var mCurrentView: View? = null

    public override fun onMeasure(widthMeasureSpec: Int, hMeasureSpec: Int) {
        var heightMeasureSpec = hMeasureSpec
        if (mCurrentView == null) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
            return
        }
        var height = 0
        mCurrentView!!.measure(
            widthMeasureSpec,
            MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        )
        val h: Int = mCurrentView!!.measuredHeight
        if (h > height) height = h
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY)
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    fun measureCurrentView(currentView: View?) {
        mCurrentView = currentView
        requestLayout()
    }

    fun measureFragment(view: View?): Int {
        if (view == null) return 0
        view.measure(0, 0)
        return view.measuredHeight
    }
}