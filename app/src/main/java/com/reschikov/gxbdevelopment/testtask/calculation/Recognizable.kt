package com.reschikov.gxbdevelopment.testtask.calculation

import java.text.ParseException

interface Recognizable {

    @Throws(ParseException::class, NumberFormatException::class, Exception::class)
    suspend fun parseStringExpression(string: String) : List<Calculatable>

    @Throws(NumberFormatException::class)
    fun parseStringData(from: String, to: String) : FloatArray
}