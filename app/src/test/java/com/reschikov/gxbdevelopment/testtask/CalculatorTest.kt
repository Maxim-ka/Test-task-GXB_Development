package com.reschikov.gxbdevelopment.testtask

import com.reschikov.gxbdevelopment.testtask.parser.ExpressionParsing
import com.reschikov.gxbdevelopment.testtask.calculation.Calculator
import com.reschikov.gxbdevelopment.testtask.parser.ModuleParser
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import kotlin.reflect.full.*
import kotlin.reflect.jvm.isAccessible

@RunWith(Parameterized::class)
class CalculatorTest(private val expression : String,
                     private val from : String,
                     private val to : String,
                     private val result : FloatArray ) {

    companion object {
        @Parameterized.Parameters
        @JvmStatic
        fun  data() : Collection<Array<*>>{
            return listOf (
                arrayOf("-x", "2", "10",
                    arrayOf(-2.0f, -2.8f, -3.6f, -4.4f, -5.2f, -6.0f, -6.8f, -7.6f, -8.4f, -9.2f, -10.0f).toFloatArray()),
                arrayOf("2x^2+7", "2", "10",
                    arrayOf(15.0f, 22.68f, 32.92f, 45.72f, 61.08f, 79.0f, 99.48f, 122.52f, 148.12f, 176.28f, 207.0f).toFloatArray()),
                arrayOf("-2.5+((x/4)^2)/(9-2x)", "-5", "9",
                    arrayOf(-2.42f, -2.45f, -2.48f, -2.496f, -2.497f, -2.45f, -2.17f, -4.9f, -3.21f, -3.08f, -3.06f).toFloatArray()),
                arrayOf("sqrt(5+(x^2))-5/x ", "-5", "9",
                    arrayOf(6.48f, 5.63f, 5.41f, 8.62f, -6.02f, 0.5f, 2.599f, 4.25f, 5.78f, 7.26f, 8.72f).toFloatArray())
            )
        }
    }

    private val calculator = Calculator(ModuleParser(ExpressionParsing()))
    private val delta = 0.01f

    @Test
    fun calculateTest(){
        runBlocking {
            calculator.takeDataAsync(expression, from, to)
            calculator.javaClass.kotlin.declaredMemberProperties.forEach {kCallable ->
                kCallable.isAccessible = true
                if (kCallable.name == "temp"){
                    Assert.assertArrayEquals(result, (kCallable.get(calculator)  as Pair<FloatArray, FloatArray>).second, delta)
                }
            }
        }
    }
}