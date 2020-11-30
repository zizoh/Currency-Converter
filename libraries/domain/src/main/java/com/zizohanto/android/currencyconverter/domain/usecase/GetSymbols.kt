package com.zizohanto.android.currencyconverter.domain.usecase

import com.zizohanto.android.currencyconverter.domain.executor.PostExecutionThread
import com.zizohanto.android.currencyconverter.domain.repository.ConverterRepository
import com.zizohanto.android.currencyconverter.domain.usecase.base.FlowUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Created by zizoh on 26/November/2020.
 */

class GetSymbols @Inject constructor(
    private val repository: ConverterRepository,
    private val postExecutionThread: PostExecutionThread
) : FlowUseCase<Unit, List<String>>() {

    override val dispatcher: CoroutineDispatcher
        get() = postExecutionThread.io

    override fun execute(params: Unit?): Flow<List<String>> {
        return repository.getSymbols()
    }
}