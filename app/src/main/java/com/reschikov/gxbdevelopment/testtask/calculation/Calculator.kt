package com.reschikov.gxbdevelopment.testtask.calculation

import com.reschikov.gxbdevelopment.testtask.calculation.operations.Result
import com.reschikov.gxbdevelopment.testtask.ui.Calculable
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlin.math.abs

private const val INDENT = 10.0f

class Calculator(private val recognizable: Recognizable) : Calculable {

    private lateinit var temp : Pair<FloatArray, FloatArray>

    @Throws(ArithmeticException::class)
    override suspend fun takeDataAsync(expression : String, from : String, to : String) : Boolean {
        return coroutineScope{
            val orderOperations : Deferred<List<Calculatable>> = async { recognizable.parseStringExpression(expression) }
            val arrayValues : Deferred<FloatArray> = async { recognizable.parseStringData(from, to) }
            orderOperations.await().calculate(arrayValues.await())
        }
    }

    @Throws(ArithmeticException::class)
    private fun List<Calculatable>.calculate(arrData: FloatArray) : Boolean {
        if (isEmpty() || arrData.isEmpty()) return false
        val results = FloatArray( arrData.size)
        var index = 0
        arrData.forEach {value ->
            forEach {cal ->  cal.calculateResult(value)}
            (last() as Result).giveResult()?.let{
                results[index++] = it
            }
        }
        temp = Pair(arrData, results)
        return true
    }

    @Throws(Exception::class)
    override suspend fun reScaleAsync(width: Int, height: Int) :
            Triple<FloatArray, FloatArray, Pair<FloatArray, FloatArray>> {
        return coroutineScope {
            val deferredX: Deferred<Pair<Float, FloatArray>> = async { recount(temp.first, width, false) }
            val deferredY: Deferred<Pair<Float, FloatArray>> = async { recount(temp.second, height, true) }
            val x = deferredX.await()
            val y = deferredY.await()
            val axisX = arrayOf(0.0f, y.first, width.toFloat(), y.first).toFloatArray()
            val axisY = arrayOf(x.first, height.toFloat(), x.first, 0.0f).toFloatArray()
            Triple(axisX, axisY, Pair(x.second, y.second))
        }
    }

    @Throws(Exception::class)
    private suspend fun recount(array: FloatArray, size: Int, isAxisY : Boolean) : Pair<Float, FloatArray>{
        val minMax = findMaxMinArrayAsync(array)
        val factor =  (size - 2 *INDENT)/ (abs(minMax.first) + abs(minMax.second))
        val zeroAxis = defineZeroAxis(minMax.first, minMax.second, size, isAxisY)
        val newArray = FloatArray(array.size)
        array.forEachIndexed{index, float ->
            val newValue = float * factor
            newArray[index] = if (isAxisY) zeroAxis - newValue else zeroAxis + newValue
        }
        return Pair(zeroAxis, newArray)
    }

    private fun defineZeroAxis(min : Float, max : Float, size: Int, isAxisY: Boolean) : Float{
        if (min >= 0 && max > 0) return if (isAxisY) size - INDENT else INDENT
        if (min < 0 && max <= 0) return if (isAxisY) INDENT else size - INDENT
        val  zero = size/(abs(max) + abs(min) ) * abs(min)
        return if (isAxisY) size - zero else zero
    }

    @Throws(Exception::class)
    private suspend fun findMaxMinArrayAsync(data: FloatArray) : Pair<Float, Float>{
        return coroutineScope {
            val deferredMin: Deferred<Float> = async { data.min() ?: throw Exception("$data min == null")}
            val deferredMax: Deferred<Float> = async { data.max() ?: throw Exception("$data max == null")}
            Pair(deferredMin.await(), deferredMax.await())
        }
    }
}