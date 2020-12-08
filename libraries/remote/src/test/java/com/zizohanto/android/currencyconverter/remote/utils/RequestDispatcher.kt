package com.zizohanto.android.currencyconverter.remote.utils

import com.zizohanto.android.currencyconverter.remote.ApiService.Companion.API_KEY
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest
import java.net.HttpURLConnection

/**
 * Created by zizoh on 07/December/2020.
 */

class RequestDispatcher : Dispatcher() {

    override fun dispatch(request: RecordedRequest): MockResponse {
        return when (request.path) {
            "$REQUEST_PATH?access_key=$API_KEY" -> {
                MockResponse()
                    .setResponseCode(HttpURLConnection.HTTP_OK)
                    .setBody(getJson("response/symbols.json"))
            }
            "/$DATE_URL?access_key=$API_KEY&symbols=$SYMBOLS" -> {
                MockResponse()
                    .setResponseCode(HttpURLConnection.HTTP_OK)
                    .setBody(getJson("response/historical_data.json"))
            }
            else -> throw IllegalArgumentException("Unknown Request Path ${request.path}")
        }
    }
}