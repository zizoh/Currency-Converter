package com.zizohanto.android.currencyconverter.converter.presentation.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by zizoh on 28/November/2020.
 */

@Parcelize
data class HistoricalDataModel(val convertedRate: Double, val time: String): Parcelable