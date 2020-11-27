package com.zizohanto.android.currencyconverter.core.di

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.zizohanto.android.currencyconverter.core.BuildConfig
import com.zizohanto.android.currencyconverter.data.contract.remote.ConverterRemote
import com.zizohanto.android.currencyconverter.remote.ApiService
import com.zizohanto.android.currencyconverter.remote.ApiServiceFactory
import com.zizohanto.android.currencyconverter.remote.impl.ConverterRemoteImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Created by zizoh on 26/November/2020.
 */

@InstallIn(SingletonComponent::class)
@Module
interface RemoteModule {

    @get:[Binds Singleton]
    val ConverterRemoteImpl.bindConverterRemote: ConverterRemote

    companion object {
        @get:[Provides Singleton]
        val provideMoshi: Moshi
            get() = Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()

        @[Provides Singleton]
        fun provideApiService(moshi: Moshi): ApiService =
            ApiServiceFactory.createApiService(BuildConfig.DEBUG, moshi)
    }
}