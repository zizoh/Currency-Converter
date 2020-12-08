package com.zizohanto.android.currencyconverter.remote.impl

import com.google.common.truth.Truth.assertThat
import com.zizohanto.android.currencyconverter.data.contract.remote.ConverterRemote
import com.zizohanto.android.currencyconverter.data.models.HistoricalDataEntity
import com.zizohanto.android.currencyconverter.remote.ApiService.Companion.API_KEY
import com.zizohanto.android.currencyconverter.remote.mapper.HistoricalDataRemoteModelMapper
import com.zizohanto.android.currencyconverter.remote.mapper.SymbolRemoteModelMapper
import com.zizohanto.android.currencyconverter.remote.utils.DATE_URL
import com.zizohanto.android.currencyconverter.remote.utils.RequestDispatcher
import com.zizohanto.android.currencyconverter.remote.utils.getJson
import com.zizohanto.android.currencyconverter.remote.utils.makeTestApiService
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.net.HttpURLConnection

/**
 * Created by zizoh on 07/December/2020.
 */

class ConverterRemoteImplTest {

    private lateinit var sut: ConverterRemote

    private lateinit var mockWebServer: MockWebServer

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.dispatcher = RequestDispatcher()
        mockWebServer.start()
        sut = ConverterRemoteImpl(
            makeTestApiService(mockWebServer),
            SymbolRemoteModelMapper(),
            HistoricalDataRemoteModelMapper()
        )
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `getSymbols returns data`() = runBlocking {
        val actual = sut.getSymbols()

        assertThat(actual.isNotEmpty()).isEqualTo(true)
        assertThat(actual[122]).isEqualTo("NGN")
    }

    @Test
    fun `getHistoricalData returns data`() = runBlocking {
        val base = "USD"
        val target = "NGN"

        val actual: HistoricalDataEntity = sut.getHistoricalData(DATE_URL, base, target)

        assertThat(actual.id).isEqualTo("$base$target")
    }
}