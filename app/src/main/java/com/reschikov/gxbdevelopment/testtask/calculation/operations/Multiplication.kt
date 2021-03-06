package com.reschikov.gxbdevelopment.testtask.calculation.operations

class Multiplication (leftOperand : Result,  rightOperand : Result) :
    Arithmetic(leftOperand, rightOperand) {

    override fun execute(left: Float, right: Float) : Float {
        return left * right
    }
}