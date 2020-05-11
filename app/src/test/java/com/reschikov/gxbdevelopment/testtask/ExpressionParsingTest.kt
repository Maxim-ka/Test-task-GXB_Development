package com.reschikov.gxbdevelopment.testtask

import com.reschikov.gxbdevelopment.testtask.parser.ExpressionParsing
import com.reschikov.gxbdevelopment.testtask.parser.ParseableExpression
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class ExpressionParsingTest(private val expression: String,
                            private val actions : String) {

    companion object {
        @Parameterized.Parameters
        @JvmStatic
        fun  data() : Collection<Array<String>>{
            return listOf (
                arrayOf(" X ",
                    "[Equally(Appropriation(null))]"),
                arrayOf(" -X ",
                    "[Multiplication(Appropriation(-1.0), Appropriation(null))]"),
                arrayOf(" x+2 ",
                    "[Addition(Appropriation(null), Appropriation(2.0))]"),
                arrayOf("-2.5+x",
                    "[Addition(Appropriation(-2.5), Appropriation(null))]"),
                arrayOf(" -x/0.5",
                    "[Multiplication(Appropriation(-1.0), Appropriation(null)), " +
                    "Division(Multiplication(Appropriation(-1.0), Appropriation(null)), Appropriation(0.5))]"),
                arrayOf("-x^2",
                    "[Exponentiation(Appropriation(null), Appropriation(2.0)), " +
                    "Multiplication(Appropriation(-1.0), Exponentiation(Appropriation(null), Appropriation(2.0)))]"),
                arrayOf("2^X+5",
                    "[Exponentiation(Appropriation(2.0), Appropriation(null)), " +
                    "Addition(Exponentiation(Appropriation(2.0), Appropriation(null)), Appropriation(5.0))]"),
                arrayOf("2+8/6-4.7*x-5^8",
                    "[Exponentiation(Appropriation(5.0), Appropriation(8.0)), " +
                    "Division(Appropriation(8.0), Appropriation(6.0)), " +
                    "Multiplication(Appropriation(4.7), Appropriation(null)), " +
                    "Addition(Appropriation(2.0), Division(Appropriation(8.0), Appropriation(6.0))), " +
                    "Subtraction(Addition(Appropriation(2.0), Division(Appropriation(8.0), Appropriation(6.0))), Multiplication(Appropriation(4.7), Appropriation(null))), " +
                    "Subtraction(Subtraction(Addition(Appropriation(2.0), Division(Appropriation(8.0), Appropriation(6.0))), Multiplication(Appropriation(4.7), Appropriation(null))), Exponentiation(Appropriation(5.0), Appropriation(8.0)))]"),
                arrayOf("-8+9x^8/4-5",
                    "[Exponentiation(Appropriation(null), Appropriation(8.0)), " +
                    "Multiplication(Appropriation(9.0), Exponentiation(Appropriation(null), Appropriation(8.0))), " +
                    "Division(Multiplication(Appropriation(9.0), Exponentiation(Appropriation(null), Appropriation(8.0))), Appropriation(4.0)), " +
                    "Addition(Appropriation(-8.0), Division(Multiplication(Appropriation(9.0), Exponentiation(Appropriation(null), Appropriation(8.0))), Appropriation(4.0))), " +
                    "Subtraction(Addition(Appropriation(-8.0), Division(Multiplication(Appropriation(9.0), Exponentiation(Appropriation(null), Appropriation(8.0))), Appropriation(4.0))), Appropriation(5.0))]"),
                arrayOf("x*(-2)+(8/5.0-(-7x)^9)",
                    "[Equally(Appropriation(-2.0)), " +
                    "Multiplication(Appropriation(-7.0), Appropriation(null)), " +
                    "Equally(Multiplication(Appropriation(-7.0), Appropriation(null))), " +
                    "Exponentiation(Equally(Multiplication(Appropriation(-7.0), Appropriation(null))), Appropriation(9.0)), " +
                    "Division(Appropriation(8.0), Appropriation(5.0)), " +
                    "Subtraction(Division(Appropriation(8.0), Appropriation(5.0)), Exponentiation(Equally(Multiplication(Appropriation(-7.0), Appropriation(null))), Appropriation(9.0))), " +
                    "Multiplication(Appropriation(null), Equally(Appropriation(-2.0))), " +
                    "Addition(Multiplication(Appropriation(null), Equally(Appropriation(-2.0))), Subtraction(Division(Appropriation(8.0), Appropriation(5.0)), Exponentiation(Equally(Multiplication(Appropriation(-7.0), Appropriation(null))), Appropriation(9.0))))]"),
                arrayOf("-5x*7+x/(3^(8x+2)-6)",
                    "[Multiplication(Appropriation(8.0), Appropriation(null)), " +
                    "Addition(Multiplication(Appropriation(8.0), Appropriation(null)), Appropriation(2.0)), " +
                    "Exponentiation(Appropriation(3.0), Addition(Multiplication(Appropriation(8.0), Appropriation(null)), Appropriation(2.0))), " +
                    "Subtraction(Exponentiation(Appropriation(3.0), Addition(Multiplication(Appropriation(8.0), Appropriation(null)), Appropriation(2.0))), Appropriation(6.0)), " +
                    "Multiplication(Appropriation(-5.0), Appropriation(null)), " +
                    "Multiplication(Multiplication(Appropriation(-5.0), Appropriation(null)), Appropriation(7.0)), " +
                    "Division(Appropriation(null), Subtraction(Exponentiation(Appropriation(3.0), Addition(Multiplication(Appropriation(8.0), Appropriation(null)), Appropriation(2.0))), Appropriation(6.0))), " +
                    "Addition(Multiplication(Multiplication(Appropriation(-5.0), Appropriation(null)), Appropriation(7.0)), Division(Appropriation(null), Subtraction(Exponentiation(Appropriation(3.0), Addition(Multiplication(Appropriation(8.0), Appropriation(null)), Appropriation(2.0))), Appropriation(6.0))))]"),
                arrayOf("5*3-4+sqrt(7^9x/(4-3x))*9",
                    "[Multiplication(Appropriation(3.0), Appropriation(null)), " +
                    "Subtraction(Appropriation(4.0), Multiplication(Appropriation(3.0), Appropriation(null))), " +
                    "Multiplication(Appropriation(9.0), Appropriation(null)), " +
                    "Exponentiation(Appropriation(7.0), Multiplication(Appropriation(9.0), Appropriation(null))), " +
                    "Division(Exponentiation(Appropriation(7.0), Multiplication(Appropriation(9.0), Appropriation(null))), Subtraction(Appropriation(4.0), Multiplication(Appropriation(3.0), Appropriation(null)))), " +
                    "RootExtraction(Division(Exponentiation(Appropriation(7.0), Multiplication(Appropriation(9.0), Appropriation(null))), Subtraction(Appropriation(4.0), Multiplication(Appropriation(3.0), Appropriation(null))))), " +
                    "Multiplication(Appropriation(5.0), Appropriation(3.0)), " +
                    "Multiplication(RootExtraction(Division(Exponentiation(Appropriation(7.0), Multiplication(Appropriation(9.0), Appropriation(null))), Subtraction(Appropriation(4.0), Multiplication(Appropriation(3.0), Appropriation(null))))), Appropriation(9.0)), " +
                    "Subtraction(Multiplication(Appropriation(5.0), Appropriation(3.0)), Appropriation(4.0)), " +
                    "Addition(Subtraction(Multiplication(Appropriation(5.0), Appropriation(3.0)), Appropriation(4.0)), Multiplication(RootExtraction(Division(Exponentiation(Appropriation(7.0), Multiplication(Appropriation(9.0), Appropriation(null))), Subtraction(Appropriation(4.0), Multiplication(Appropriation(3.0), Appropriation(null))))), Appropriation(9.0)))]"),
                arrayOf("-SQRT(x)",
                    "[Equally(Appropriation(null)), " +
                    "RootExtraction(Equally(Appropriation(null))), " +
                    "Multiplication(Appropriation(-1.0), RootExtraction(Equally(Appropriation(null))))]"),
                arrayOf("-sqrt(5-(4/2))*x^sqrt((10/7)*sqrt(8-7x))",
                    "[Division(Appropriation(4.0), Appropriation(2.0)), " +
                    "Subtraction(Appropriation(5.0), Division(Appropriation(4.0), Appropriation(2.0))), " +
                    "Division(Appropriation(10.0), Appropriation(7.0)), " +
                    "Multiplication(Appropriation(7.0), Appropriation(null)), " +
                    "Subtraction(Appropriation(8.0), Multiplication(Appropriation(7.0), Appropriation(null))), " +
                    "RootExtraction(Subtraction(Appropriation(8.0), Multiplication(Appropriation(7.0), Appropriation(null)))), " +
                    "Multiplication(Division(Appropriation(10.0), Appropriation(7.0)), RootExtraction(Subtraction(Appropriation(8.0), Multiplication(Appropriation(7.0), Appropriation(null))))), " +
                    "RootExtraction(Subtraction(Appropriation(5.0), Division(Appropriation(4.0), Appropriation(2.0)))), " +
                    "RootExtraction(Multiplication(Division(Appropriation(10.0), Appropriation(7.0)), RootExtraction(Subtraction(Appropriation(8.0), Multiplication(Appropriation(7.0), Appropriation(null)))))), " +
                    "Exponentiation(Appropriation(null), RootExtraction(Multiplication(Division(Appropriation(10.0), Appropriation(7.0)), RootExtraction(Subtraction(Appropriation(8.0), Multiplication(Appropriation(7.0), Appropriation(null))))))), " +
                    "Multiplication(Appropriation(-1.0), RootExtraction(Subtraction(Appropriation(5.0), Division(Appropriation(4.0), Appropriation(2.0))))), " +
                    "Multiplication(Multiplication(Appropriation(-1.0), RootExtraction(Subtraction(Appropriation(5.0), Division(Appropriation(4.0), Appropriation(2.0))))), Exponentiation(Appropriation(null), RootExtraction(Multiplication(Division(Appropriation(10.0), Appropriation(7.0)), RootExtraction(Subtraction(Appropriation(8.0), Multiplication(Appropriation(7.0), Appropriation(null))))))))]"),
                arrayOf("-2.5+(x/4)^2/(9-2x)",
                    "[Division(Appropriation(null), Appropriation(4.0)), " +
                    "Multiplication(Appropriation(2.0), Appropriation(null)), " +
                    "Subtraction(Appropriation(9.0), Multiplication(Appropriation(2.0), Appropriation(null))), " +
                    "Exponentiation(Division(Appropriation(null), Appropriation(4.0)), Appropriation(2.0)), " +
                    "Division(Exponentiation(Division(Appropriation(null), Appropriation(4.0)), Appropriation(2.0)), Subtraction(Appropriation(9.0), Multiplication(Appropriation(2.0), Appropriation(null)))), " +
                    "Addition(Appropriation(-2.5), Division(Exponentiation(Division(Appropriation(null), Appropriation(4.0)), Appropriation(2.0)), Subtraction(Appropriation(9.0), Multiplication(Appropriation(2.0), Appropriation(null)))))]"),
                arrayOf("-(X^2+6*56)",
                    "[Exponentiation(Appropriation(null), Appropriation(2.0)), " +
                    "Multiplication(Appropriation(6.0), Appropriation(56.0)), " +
                    "Addition(Exponentiation(Appropriation(null), Appropriation(2.0)), Multiplication(Appropriation(6.0), Appropriation(56.0))), " +
                    "Multiplication(Appropriation(-1.0), Addition(Exponentiation(Appropriation(null), Appropriation(2.0)), Multiplication(Appropriation(6.0), Appropriation(56.0))))]")
            )
        }
    }

    private val parsed : ParseableExpression = ExpressionParsing()


    @Test
    fun defineActionExpressionTest(){
        runBlocking {
            parsed.checkStringExpressionAsync(expression)
            Assert.assertEquals(actions, parsed.defineActionExpression(expression).toString())
        }
    }
}