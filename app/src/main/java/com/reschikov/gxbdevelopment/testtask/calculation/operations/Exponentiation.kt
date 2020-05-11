package com.reschikov.gxbdevelopment.testtask.calculation.operations

import java.lang.ArithmeticException
import kotlin.math.pow

class Exponentiation  (leftOperand : Result,  rightOperand : Result) :
    Arithmetic(leftOperand, rightOperand) {

    @Throws(ArithmeticException::class)
    override fun execute(left: Float, right: Float) : Float {
        return left.pow(right)
    }
}