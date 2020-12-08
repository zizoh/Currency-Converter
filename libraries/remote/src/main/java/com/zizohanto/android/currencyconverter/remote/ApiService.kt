package com.zizohanto.android.currencyconverter.remote

import com.zizohanto.android.currencyconverter.remote.model.HistoricalDataRemoteModel
import com.zizohanto.android.currencyconverter.remote.model.SymbolRemoteModel
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by zizoh on 25/November/2020.
 */

interface ApiService {

    @GET("symbols")
    suspend fun getSymbols(
        @Query("access_key") apiKey: String = API_KEY
    ): SymbolRemoteModel

    @GET("/{date}")
    suspend fun getHistoricalData(
        @Path("date") date: String,
        @Query("access_key") apiKey: String = API_KEY,
        @Query("symbols") symbols: String
    ): HistoricalDataRemoteModel

    companion object {
        const val API_KEY = ""
    }
}