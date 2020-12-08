package com.zizohanto.android.currencyconverter.domain.usecase

import com.google.common.truth.Truth.assertThat
import com.zizohanto.android.currencyconverter.domain.executor.TestPostExecutionThread
import com.zizohanto.android.currencyconverter.domain.repository.ConverterRepositoryImplFake
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

/**
 * Created by zizoh on 07/December/2020.
 */

class GetSymbolsTest{

    private val sut = GetSymbols(ConverterRepositoryImplFake(), TestPostExecutionThread())

    @Test
    fun `getSymbols returns list of symbols`() = runBlockingTest {

        val actual: List<String> = sut().first()

        assertThat(actual.isNotEmpty()).isEqualTo(true)
    }
}