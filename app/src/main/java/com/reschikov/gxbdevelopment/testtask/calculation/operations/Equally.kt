package com.reschikov.gxbdevelopment.testtask.calculation.operations

import com.reschikov.gxbdevelopment.testtask.calculation.Calculatable

open class Equally (private val operand : Result) : Result(), Calculatable {

    override fun calculateResult(value: Float) {
        result = operand.giveResult()?.run { this } ?: value
    }

    override fun toString(): String {
        return "${this.javaClass.simpleName}(${operand})"
    }
}