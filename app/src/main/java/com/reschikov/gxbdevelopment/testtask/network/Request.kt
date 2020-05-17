package com.reschikov.gxbdevelopment.testtask.network

import com.reschikov.gxbdevelopment.testtask.ui.Derivable
import com.reschikov.gxbdevelopment.testtask.wolfram.Requestable
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class Request(private val retrofitable: Retrofitable) : Derivable{

    override fun getRetrofitable(): Retrofitable = retrofitable

    override fun <T> getCallBack(continuation: Continuation<T>) : Callback<T> {
        return object : Callback<T> {
            override fun onFailure(call: Call<T>, t: Throwable) {
                continuation.resumeWithException(t)
            }

            override fun onResponse(call: Call<T>, response: Response<T>) {
                if(response.isSuccessful){
                    response.body()?.let { continuation.resume(it) }
                } else {
                    response.errorBody()?.let { continuation.resumeWithException(Throwable(it.string())) }
                }
            }
        }
    }

    override suspend fun<T> derive(requestable: Requestable<T>) : T {
        return  requestable.executeRequest(this)
    }
}