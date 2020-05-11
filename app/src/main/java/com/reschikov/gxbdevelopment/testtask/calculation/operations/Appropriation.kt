package com.reschikov.gxbdevelopment.testtask.calculation.operations

class Appropriation(private val number : Float?) : Result() {

    override fun giveResult()= number

    override fun toString(): String {
        return "Appropriation(${number})"
    }
}
