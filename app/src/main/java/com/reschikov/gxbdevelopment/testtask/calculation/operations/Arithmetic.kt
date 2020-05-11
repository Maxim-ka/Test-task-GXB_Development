package com.reschikov.gxbdevelopment.testtask.calculation.operations

import com.reschikov.gxbdevelopment.testtask.calculation.Calculatable
import java.lang.ArithmeticException

abstract class Arithmetic(private val leftOperand : Result,
                 private val rightOperand : Result) : Result(), Calculatable {

    private var left : Float = 0.0f
    private var right : Float = 0.0f

    @Throws(ArithmeticException::class)
    override fun calculateResult(value: Float) {
        val left =  leftOperand.giveResult()?.run { this }?: value
        val right = rightOperand.giveResult()?.run { this }?: value
        if (checkValues(left, right)) return
        result = execute(left, right)
        setValues(left, right)
    }

    private fun setValues(left: Float, right: Float){
        this.left = left
        this.right = right
    }

    private fun checkValues(left: Float, right: Float) : Boolean {
        return result != null && this.left.compareTo(left) == 0 && this.right.compareTo(right) == 0
    }

    @Throws(ArithmeticException::class)
    abstract fun execute(left: Float, right: Float) : Float

    override fun equals(other: Any?): Boolean {
        if (other == null) return false
        if (other is Arithmetic) {
            return leftOperand == other.leftOperand && rightOperand == other.rightOperand
        }
        return false
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }

    override fun toString(): String {
        return "${this.javaClass.simpleName}($leftOperand, $rightOperand)"
    }
}