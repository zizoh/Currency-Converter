package com.zizohanto.android.currencyconverter.core.di

import com.zizohanto.android.currencyconverter.data.repository.ConverterRepositoryImpl
import com.zizohanto.android.currencyconverter.domain.repository.ConverterRepository
import com.zizohanto.android.currencyconverter.domain.repository.ConverterRepositoryImplFake
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Created by zizoh on 26/November/2020.
 */

@InstallIn(SingletonComponent::class)
@Module
interface DataModule {

    @get:Binds
    val ConverterRepositoryImpl.converterRepository: ConverterRepository

}