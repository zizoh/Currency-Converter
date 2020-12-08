package com.zizohanto.android.currencyconverter.converter.presentation.mappers

import com.google.common.truth.Truth.assertThat
import com.zizohanto.android.currencyconverter.converter.presentation.factory.ConverterFactory.makeHistoricalData
import org.junit.Test

/**
 * Created by zizoh on 07/December/2020.
 */

class HistoricalDataModelMapperTest {

    private val sut = HistoricalDataModelMapper()

    @Test
    fun mapToModel() {
        val expectedConvertedRate = 1759.0
        val actual = sut.mapToModel(
            makeHistoricalData().copy(
                convertedRate = expectedConvertedRate,
                timeStamp = 1606755597
            )
        )

        assertThat(actual.convertedRate).isEqualTo(expectedConvertedRate)
        assertThat(actual.time).isEqualTo("17:59 WAT")
    }

    @Test
    fun getFormattedTime() {
        val actual = sut.getFormattedTime(1606755597)

        assertThat(actual).isEqualTo("17:59 WAT")
    }
}