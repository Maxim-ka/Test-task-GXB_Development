package com.reschikov.gxbdevelopment.testtask

import com.reschikov.gxbdevelopment.testtask.parser.ExpressionParsing
import com.reschikov.gxbdevelopment.testtask.parser.ModuleParser
import com.reschikov.gxbdevelopment.testtask.calculation.Recognizable
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import java.lang.Exception

@RunWith(Parameterized::class)
class ModuleParserTest(private val from : String,
                      private val to : String,
                      private val array : FloatArray,
                      private val errMessage : String) {

    companion object {
        @Parameterized.Parameters
        @JvmStatic
        fun  data() : Collection<Array<*>>{
            return listOf (
                arrayOf("2", "10",
                    arrayOf(2.0f, 2.8f, 3.6f, 4.4f, 5.2f, 6.0f, 6.8f, 7.6f, 8.4f, 9.2f, 10.0f).toFloatArray(),
                    "start 10 >= end 2"),
                arrayOf("2", "20",
                    arrayOf(2.0f, 3.8f, 5.6f, 7.4f, 9.2f, 11.0f, 12.8f, 14.6f, 16.4f, 18.2f, 20.0f).toFloatArray(),
                    "start 20 >= end 2"),
                arrayOf("-5", "9",
                    arrayOf(-5.0f, -3.6f, -2.2f, -0.8f, 0.6f, 2.0f, 3.4f, 4.8f, 6.2f, 7.6f, 9.0f).toFloatArray(),
                    "start 9 >= end -5"),
                arrayOf("-13", "-1",
                    arrayOf(-13.0f, -12.08f, -11.16f, -10.24f, -9.32f, -8.4f, -7.48f, -6.56f, -5.64f, -4.72f, -3.8f, -2.88f, -1.96f, -1.0f).toFloatArray(),
                    "start -1 >= end -13"),
                arrayOf("1.2", "11.7",
                    arrayOf(1.2f, 2.25f, 3.3f, 4.35f, 5.4f, 6.45f, 7.5f, 8.55f, 9.6f, 10.65f, 11.7f).toFloatArray(),
                    "start 11.7 >= end 1.2"),
                arrayOf("-15.75", "5.35",
                    arrayOf(-15.75f, -13.64f, -11.53f, -9.42f, -7.31f, -5.2f, -3.09f, -0.98f, 1.13f, 3.24f, 5.35f).toFloatArray(),
                    "start 5.35 >= end -15.75")
            )
        }
    }

    private val recognizable : Recognizable = ModuleParser(ExpressionParsing())
    private val delta = 0.1f

    @Test
    fun parseStringDataTest(){
        Assert.assertArrayEquals(array, recognizable.parseStringData(from, to), delta)
    }

    @Test
    fun parseStringDataExceptionTest(){
        try {
            recognizable.parseStringData(to, from)
        }catch (e : Exception){
            Assert.assertEquals(errMessage, e.message)
        }
    }
}