package com.zizohanto.android.currencyconverter.data.factory

import com.zizohanto.android.currencyconverter.data.models.HistoricalDataEntity
import java.util.*

/**
 * Created by zizoh on 07/December/2020.
 */

object DataFactory {
    fun makeHistoricalDataEntity(): HistoricalDataEntity {
        return HistoricalDataEntity(
            "USDNGN",
            9563781L,
            "USD",
            "2020-11-30",
            1.164151,
            442.96153
        )
    }
}