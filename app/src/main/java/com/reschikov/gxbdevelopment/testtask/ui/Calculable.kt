package com.reschikov.gxbdevelopment.testtask.ui

import java.lang.ArithmeticException

interface Calculable {

    @Throws(ArithmeticException::class)
    suspend fun takeDataAsync(expression : String, from : String, to : String) : Boolean

    suspend fun reScaleAsync(width: Int, height: Int) :
            Triple<FloatArray, FloatArray, Pair<FloatArray, FloatArray>>

}