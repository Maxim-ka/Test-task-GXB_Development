package com.reschikov.gxbdevelopment.testtask.wolfram

import com.reschikov.gxbdevelopment.testtask.wolfram.model.Queryresult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WolframAlpha {

    @GET("query")
    fun getGraphFunction(@Query ("appid") appid : String,
                         @Query("input") input : String,
                         @Query ("includepodid") includepodid : String) : Call<Queryresult>
}