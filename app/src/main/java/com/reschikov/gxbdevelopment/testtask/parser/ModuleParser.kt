package com.reschikov.gxbdevelopment.testtask.parser

import com.reschikov.gxbdevelopment.testtask.calculation.Calculatable
import com.reschikov.gxbdevelopment.testtask.calculation.Recognizable
import java.text.ParseException
import kotlin.math.abs

private const val MIN_NUMBER_POINT = 10

class ModuleParser(private val parsedExpression: ParseableExpression) :
    Recognizable {

    private var expression : String? = null
    private var from : String? = null
    private var to : String? = null
    private lateinit var listOfOperations : List<Calculatable>
    private lateinit var arrayValues : FloatArray

    @Throws(ParseException::class, NumberFormatException::class, Exception::class)
    override suspend fun parseStringExpression(string: String) : List<Calculatable>{
        if (string == expression) return listOfOperations
        if (parsedExpression.checkStringExpressionAsync(string)) {
            listOfOperations = parsedExpression.defineActionExpression(string)
            expression = string
            return listOfOperations
        }
        throw Exception(string)
    }

    @Throws(NumberFormatException::class)
    override fun parseStringData(from: String, to: String): FloatArray {
        if (this.from == from && this.to == to) return arrayValues
        val start = convertToNumber(from)
        val end = convertToNumber(to)
        if (start >= end) throw Exception("start $from >= end $to")
        this.from = from
        this.to = to
        return createData(start, end)
    }

    @Throws(NumberFormatException::class)
    private fun convertToNumber(string: String) : Float{
        return string.toFloat()
    }

    private fun createData(start: Float, end: Float) : FloatArray{
        arrayValues = createControlPoints(start, end)
        return arrayValues
    }

    private fun createControlPoints(start: Float, end: Float) : FloatArray{
        val numberPoint  = determineNumberOfPoints(start, end)
        val array = FloatArray(numberPoint + 1)
        val step = (end - start) / numberPoint
        var value = start
        repeat(numberPoint){
            array[it] = value
            value += step
        }
        array[array.lastIndex] = end
        return array
    }

    private fun determineNumberOfPoints(start: Float, end: Float) : Int {
        val numberPoint : Int = when {
            start == 0.0f -> end.toInt()
            end == 0.0f -> start.toInt()
            abs(end) < abs(start) -> (abs(start) / abs(end)).toInt()
            else -> (abs(end) / abs(start)).toInt()
        }
        return if(numberPoint < MIN_NUMBER_POINT)  MIN_NUMBER_POINT else numberPoint
    }
}