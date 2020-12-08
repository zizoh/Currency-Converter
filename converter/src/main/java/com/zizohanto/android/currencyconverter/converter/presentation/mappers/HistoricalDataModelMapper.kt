package com.zizohanto.android.currencyconverter.converter.presentation.mappers

import com.zizohanto.android.currencyconverter.converter.presentation.models.HistoricalDataModel
import com.zizohanto.android.currencyconverter.domain.models.HistoricalData
import com.zizohanto.android.currencyconverter.presentation.mapper.ModelMapper
import org.joda.time.DateTime
import javax.inject.Inject

/**
 * Created by zizoh on 28/November/2020.
 */

class HistoricalDataModelMapper @Inject constructor() :
    ModelMapper<HistoricalDataModel, HistoricalData> {

    override fun mapToModel(domain: HistoricalData): HistoricalDataModel {
        return HistoricalDataModel(domain.convertedRate, getFormattedTime(domain.timeStamp))
    }

    override fun mapToDomain(model: HistoricalDataModel): HistoricalData {
        throw IllegalStateException("Calm down, ya not going that way son.")
    }

    fun getFormattedTime(timeStamp: Long): String {
        val dateTime = DateTime(timeStamp * 1000L)
        val time = dateTime.toString("HH:mm")
        val timeZone: String = getThreeLetterTimeZone(dateTime)
        return "$time $timeZone"
    }

    private fun getThreeLetterTimeZone(dateTime: DateTime): String {
        return "WAT"
    }
}