package com.zizohanto.android.currencyconverter.remote

import com.squareup.moshi.Moshi
import com.zizohanto.android.currencyconverter.remote.interceptor.HttpsInterceptor
import com.zizohanto.android.currencyconverter.remote.interceptor.NoInternetInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by zizoh on 25/November/2020.
 */

object ApiServiceFactory {

    private const val BASE_URL: String = "http://data.fixer.io/api/"

    fun createApiService(isDebug: Boolean, moshi: Moshi): ApiService {
        val okHttpClient: OkHttpClient = makeOkHttpClient(
            makeLoggingInterceptor((isDebug))
        )
        return makeApiService(okHttpClient, moshi)
    }

    private fun makeApiService(okHttpClient: OkHttpClient, moshi: Moshi): ApiService {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
        return retrofit.create(ApiService::class.java)
    }

    private fun makeOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HttpsInterceptor)
            .addInterceptor(NoInternetInterceptor)
            .addInterceptor(httpLoggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    private fun makeLoggingInterceptor(isDebug: Boolean): HttpLoggingInterceptor {
        val logging = HttpLoggingInterceptor()
        logging.level = if (isDebug) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
        return logging
    }
}