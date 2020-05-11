package com.reschikov.gxbdevelopment.testtask.calculation.operations

import java.lang.ArithmeticException

class Division (leftOperand : Result,  rightOperand : Result) :
    Arithmetic(leftOperand, rightOperand) {

    @Throws(ArithmeticException::class)
    override fun execute(left: Float, right: Float) : Float {
        if (right == 0.0f) throw ArithmeticException("$left/0")
        return left / right
    }
}