package com.zizohanto.android.currencyconverter.cache.mappers

import com.zizohanto.android.currencyconverter.cache.mappers.base.CacheModelMapper
import com.zizohanto.android.currencyconverter.cache.models.HistoricalDataCacheModel
import com.zizohanto.android.currencyconverter.data.models.HistoricalDataEntity
import javax.inject.Inject

/**
 * Created by zizoh on 28/November/2020.
 */

class HistoricalDataCacheModelMapper @Inject constructor() :
    CacheModelMapper<HistoricalDataCacheModel, HistoricalDataEntity> {

    override fun mapToModel(entity: HistoricalDataEntity): HistoricalDataCacheModel {
        return entity.run {
            HistoricalDataCacheModel(
                id,
                timestamp,
                base,
                date,
                oneEuroToBaseRate,
                oneEuroToTargetRate
            )
        }
    }

    override fun mapToEntity(model: HistoricalDataCacheModel): HistoricalDataEntity {
        return model.run {
            HistoricalDataEntity(
                id,
                timestamp,
                base,
                date,
                oneEuroToBaseRate,
                oneEuroToTargetRate
            )
        }
    }
}