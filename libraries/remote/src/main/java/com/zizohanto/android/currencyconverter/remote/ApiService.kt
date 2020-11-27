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
        @Query("access_key") apiKey: String = ""
    ): SymbolRemoteModel

    @GET("symbols/{date}")
    suspend fun getHistoricalData(
        @Path("date") date: String,
        @Query("access_key") apiKey: String = "",
        @Query("symbols") symbols: List<String>
    ): HistoricalDataRemoteModel
}