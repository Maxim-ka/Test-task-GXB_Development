package com.reschikov.gxbdevelopment.testtask.parser

import com.reschikov.gxbdevelopment.testtask.calculation.Calculatable

interface ParseableExpression {

    suspend fun checkStringExpressionAsync(string: String) : Boolean
    fun defineActionExpression(string: String) : List<Calculatable>
}