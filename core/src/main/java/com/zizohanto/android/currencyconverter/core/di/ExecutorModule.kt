package com.zizohanto.android.currencyconverter.core.di

import com.zizohanto.android.currencyconverter.core.executor.PostExecutionThreadImpl
import com.zizohanto.android.currencyconverter.domain.executor.PostExecutionThread
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
interface ExecutorModule {

    @get:[Binds Singleton]
    val PostExecutionThreadImpl.postExecutionThread: PostExecutionThread
}