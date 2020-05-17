package com.reschikov.gxbdevelopment.testtask.ui

import com.reschikov.gxbdevelopment.testtask.network.Retrofitable
import com.reschikov.gxbdevelopment.testtask.wolfram.Requestable
import retrofit2.Callback
import kotlin.coroutines.Continuation

interface Derivable {

    fun <T> getCallBack(continuation: Continuation<T>): Callback<T>
    suspend fun <T> derive(requestable: Requestable<T>) : T
    fun getRetrofitable() : Retrofitable
}