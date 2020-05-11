package com.reschikov.gxbdevelopment.testtask.calculation.operations

import kotlin.math.sqrt

class RootExtraction(private val operand : Result) : Equally(operand) {

    @Throws(ArithmeticException::class)
    override fun calculateResult(value: Float) {
        toExtract(operand.giveResult()?.run { this } ?: value)
    }

    @Throws(ArithmeticException::class)
    private fun toExtract(value: Float){
        if (value < 0) throw ArithmeticException("SQRT($value)")
        result = sqrt(value)
    }
}