package com.zizohanto.android.currencyconverter.domain.usecase

import com.zizohanto.android.currencyconverter.domain.exception.requireParams
import com.zizohanto.android.currencyconverter.domain.executor.PostExecutionThread
import com.zizohanto.android.currencyconverter.domain.models.HistoricalData
import com.zizohanto.android.currencyconverter.domain.repository.ConverterRepository
import com.zizohanto.android.currencyconverter.domain.usecase.GetConversionForPeriod.Params
import com.zizohanto.android.currencyconverter.domain.usecase.base.FlowUseCase
import com.zizohanto.android.currencyconverter.domain.utils.DateUtils
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Created by zizoh on 01/December/2020.
 */

class GetConversionForPeriod @Inject constructor(
    private val repository: ConverterRepository,
    private val postExecutionThread: PostExecutionThread
) : FlowUseCase<Params, List<HistoricalData>>() {

    override val dispatcher: CoroutineDispatcher
        get() = postExecutionThread.io

    override fun execute(params: Params?): Flow<List<HistoricalData>> {
        requireParams(params)
        val dates: List<String> = DateUtils.getDatesWithinPeriod(params.numberOfDays)
        return flow {
            emit(
                repository.getRatesWithinPeriod(
                    dates,
                    params.baseRate,
                    params.targetRate
                )
            )
        }
    }

    data class Params(val numberOfDays: Int, val baseRate: String, val targetRate: String)
}
