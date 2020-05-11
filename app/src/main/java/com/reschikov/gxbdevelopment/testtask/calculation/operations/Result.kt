package com.reschikov.gxbdevelopment.testtask.calculation.operations

abstract class Result : Markable {

    protected var result : Float? = null
    private lateinit var mark : String

    open fun giveResult() : Float?  = result

    override fun setMark(string: String) { mark = string }

    override fun leaveMark() : String = mark

    override fun equals(other: Any?): Boolean {
        if (other == null) return false
        if (other is Result){
            return giveResult().toString() == other.giveResult().toString()
        }
        return false
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }
}