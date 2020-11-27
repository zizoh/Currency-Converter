package com.zizohanto.android.currencyconverter.remote.mapper

import com.zizohanto.android.currencyconverter.data.models.HistoricalDataEntity
import com.zizohanto.android.currencyconverter.remote.mapper.base.RemoteModelMapper
import com.zizohanto.android.currencyconverter.remote.model.HistoricalDataRemoteModel
import javax.inject.Inject

/**
 * Created by zizoh on 25/November/2020.
 */

class HistoricalDataRemoteModelMapper @Inject constructor():
    RemoteModelMapper<HistoricalDataRemoteModel, HistoricalDataEntity> {

    override fun mapFromModel(model: HistoricalDataRemoteModel): HistoricalDataEntity {
        return HistoricalDataEntity(model.timestamp, model.base, model.date, model.rates)
    }
}