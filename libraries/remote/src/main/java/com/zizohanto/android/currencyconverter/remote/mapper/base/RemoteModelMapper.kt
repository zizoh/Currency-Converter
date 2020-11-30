package com.zizohanto.android.currencyconverter.remote.mapper.base

interface RemoteModelMapper<in M, out E> {

    fun mapFromModel(model: M): E
}
