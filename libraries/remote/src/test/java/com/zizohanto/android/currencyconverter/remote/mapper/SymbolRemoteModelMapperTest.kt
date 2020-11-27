package com.zizohanto.android.currencyconverter.remote.mapper

import com.google.common.truth.Truth.assertThat
import org.junit.Test

/**
 * Created by zizoh on 27/November/2020.
 */

class SymbolRemoteModelMapperTest {

    private val sut: SymbolRemoteModelMapper = SymbolRemoteModelMapper()

    @Test
    fun `testGetSymbols returns empty list if string is empty`() {
        val actual = sut.getSymbols("")

        assertThat(actual).isEqualTo(emptyList<String>())
    }

}