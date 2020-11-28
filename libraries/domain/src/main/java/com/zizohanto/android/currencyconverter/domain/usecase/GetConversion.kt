package com.zizohanto.android.currencyconverter.domain.usecase

import com.zizohanto.android.currencyconverter.domain.exception.requireParams
import com.zizohanto.android.currencyconverter.domain.executor.PostExecutionThread
import com.zizohanto.android.currencyconverter.domain.repository.ConverterRepository
import com.zizohanto.android.currencyconverter.domain.usecase.GetConversion.Params
import com.zizohanto.android.currencyconverter.domain.usecase.base.FlowUseCase
import com.zizohanto.android.currencyconverter.domain.utils.DateUtils
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Created by zizoh on 26/November/2020.
 */

class GetConversion @Inject constructor(
    private val repository: ConverterRepository,
    private val postExecutionThread: PostExecutionThread
) : FlowUseCase<Params, Double>() {

    override val dispatcher: CoroutineDispatcher
        get() = postExecutionThread.io

    override fun execute(params: Params?): Flow<Double> {
        requireParams(params)
        val date = DateUtils.getCurrentDate()
        return repository.getRates(date, params.amount, params.baseRate, params.targetRate)
    }

    data class Params(val amount: Double, val baseRate: String, val targetRate: String)
}
