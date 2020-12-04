package com.zizohanto.android.currencyconverter.converter.presentation.views

import android.content.Context
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.widget.ScrollView
import kotlin.math.abs


/**
 * Created by zizoh on 04/December/2020.
 */

class CustomScrollView(context: Context?, attrs: AttributeSet?) :
    ScrollView(context, attrs) {

    private val mGestureDetector: GestureDetector

    init {
        mGestureDetector = GestureDetector(context, YScrollDetector())
        setFadingEdgeLength(0)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return (super.onInterceptTouchEvent(ev)
                && mGestureDetector.onTouchEvent(ev))
    }

    // Return false if we're scrolling in the x direction
    internal inner class YScrollDetector : SimpleOnGestureListener() {
        override fun onScroll(
            e1: MotionEvent?, e2: MotionEvent?,
            distanceX: Float, distanceY: Float
        ): Boolean {
            return abs(distanceY) > abs(distanceX)
        }
    }
}