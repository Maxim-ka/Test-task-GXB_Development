package com.reschikov.gxbdevelopment.testtask.network.wolfram

import com.reschikov.gxbdevelopment.testtask.network.Retrofitable
import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory

private const val URL_SERVER = "https://api.wolframalpha.com/v2/"

class WolframRetrofit : Retrofitable{

    private val retrofit = Retrofit
        .Builder()
        .baseUrl(URL_SERVER)
        .addConverterFactory(SimpleXmlConverterFactory.create())
        .build()

    override fun getRetrofit(): Retrofit = retrofit
}