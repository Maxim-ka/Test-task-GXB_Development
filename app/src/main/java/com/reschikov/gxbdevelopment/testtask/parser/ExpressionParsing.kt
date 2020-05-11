package com.reschikov.gxbdevelopment.testtask.parser

import com.reschikov.gxbdevelopment.testtask.*
import com.reschikov.gxbdevelopment.testtask.calculation.Calculatable
import com.reschikov.gxbdevelopment.testtask.calculation.operations.*
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import java.text.ParseException
import java.util.*

private const val LEFT_BRACKET = '('
private const val RIGHT_BRACKET = ')'
private const val EMPTY_STRING = " "
private const val NEGATIVE = "-1"
private const val NAN = -1

class ExpressionParsing :
    ParseableExpression {

    private val operationsList = LinkedList<Char>()
    private val dataList = LinkedList<String>()
    private var hasBrackets = false
    private lateinit var actionList : MutableList<Calculatable>

    override suspend fun checkStringExpressionAsync(string: String) : Boolean{
        return coroutineScope {
            val checkX: Deferred<Boolean> = async{ hasStringVariableX(string)}
            val checkBracket: Deferred<Boolean> = async{ hasDoubleBrackets(string)}
            checkX.await() && checkBracket.await()
        }
    }

    private fun hasStringVariableX(string: String) : Boolean{
        return string.contains(X, true)
    }

    private fun hasDoubleBrackets(string: String) : Boolean{
        var leftBrackets = 0
        var rightBrackets = 0
        string.forEach {char ->
            if (char == LEFT_BRACKET) ++leftBrackets
            if (char == RIGHT_BRACKET){
                ++rightBrackets
                if (leftBrackets < rightBrackets) return false
            }
        }
        hasBrackets = leftBrackets != 0 && rightBrackets != 0 && leftBrackets == rightBrackets
        return leftBrackets == rightBrackets
    }

    @Throws(ParseException::class, NumberFormatException::class, Exception::class)
    override fun defineActionExpression(string: String): List<Calculatable> {
        actionList = mutableListOf()
        if (hasBrackets) {
            parseStringWithBrackets(string)
        } else {
            determineCourseOfAction(string)
        }
        return actionList
    }

    @Throws(ParseException::class, NumberFormatException::class, Exception::class)
    private fun parseStringWithBrackets(string: String){
        var str = string
        do {
            val sub = defineStringInsideBrackets(str)
            if (sub != EMPTY_STRING) str = str.replace(sub, (actionList.last() as Markable).leaveMark())
        } while (sub != EMPTY_STRING)
    }

    @Throws(ParseException::class, NumberFormatException::class, Exception::class)
    private fun defineStringInsideBrackets(string: String) : String {
        var indexLeftBracket = NAN
        string.forEachIndexed { index, char ->
            if (char == LEFT_BRACKET)  indexLeftBracket = index
            if (char == RIGHT_BRACKET){
                if (indexLeftBracket == NAN) throw ParseException(string, index)
                determineCourseOfAction(string.substring(indexLeftBracket + 1, index))
                return string.substring(indexLeftBracket, index + 1)
            }
        }
        if (indexLeftBracket == NAN) determineCourseOfAction(string)
        return EMPTY_STRING
    }

    @Throws(ParseException::class, NumberFormatException::class, Exception::class)
    private fun determineCourseOfAction(string: String){
        val cropped =  string.toLowerCase(Locale.ROOT).trim()
        val str = determineNegativity(cropped)
        selectDataFromString(str)
        selectOperationFromString(str)
        if (cropped.startsWith(MINUS)) correctNegativity()
        if (checkListOperations()) return
        if (hasBrackets) findRootExtraction()
        findExponentiation()
        findMultiplicationAndDivision()
        findAdditionAndSubtraction()
    }

    private fun determineNegativity(cropped: String) : String {
        if (cropped.startsWith(MINUS)) {
            return cropped.substring(INDEX_FIRST_ELEMENT + 1)
        }
        return cropped
    }

    private fun correctNegativity(){
        val str = dataList[INDEX_FIRST_ELEMENT]
        if (str.endsWith(Y) || str.startsWith(SQRT, true) || str.startsWith(X, true)){
            operationsList.addFirst(MULTI)
            dataList.addFirst(NEGATIVE)
        } else{
            dataList[INDEX_FIRST_ELEMENT] = MINUS + str
        }
    }

    @Throws(ParseException::class)
    private fun selectDataFromString(string: String){
        if (dataList.isNotEmpty()) dataList.clear()
        string.split(PLUS, MINUS, MULTI, DIV, EXP).map {sub ->
            if (sub.isEmpty() || sub.isBlank() || sub.contains(EMPTY_STRING)) throw ParseException(string, string.indexOf(sub))
            sub.trim()
        }.toCollection(dataList)
    }

    private fun selectOperationFromString(string: String){
        if (operationsList.isNotEmpty()) operationsList.clear()
        string.toList().filter {char ->
            char == PLUS || char == MINUS || char == MULTI || char == DIV || char == EXP
        }.toCollection(operationsList)
    }

    @Throws(ParseException::class, NumberFormatException::class, Exception::class)
    private fun checkListOperations() : Boolean {
        if (operationsList.isEmpty() && !dataList[INDEX_FIRST_ELEMENT].startsWith(SQRT, true)) {
            addAction(Equally(checkString(dataList[INDEX_FIRST_ELEMENT])))
            return true
        }
        return false
    }

    private fun findRootExtraction(){
        dataList.find {str -> str.startsWith(SQRT, true) }?.run {
            val expression = substring(SQRT.length)
            addAction(RootExtraction(checkString(expression)), dataList.indexOf(this))
            dataList.remove(this)
            findRootExtraction()
        }
    }

    @Throws(ParseException::class, NumberFormatException::class, Exception::class)
    private fun findAdditionAndSubtraction(){
        operationsList.firstOrNull {char -> char == PLUS || char == MINUS }?.let {
            if (it == PLUS){
                val index = operationsList.indexOf(it)
                val results = createResults(index)
                addAction(Addition(results.first, results.second), index)
                if (operationsList.isNotEmpty()){
                    findAdditionAndSubtraction()
                }
            }
            if (it == MINUS){
                val index = operationsList.indexOf(it)
                val results = createResults(index)
                addAction(Subtraction(results.first, results.second), index)
                if (operationsList.isNotEmpty()){
                    findAdditionAndSubtraction()
                }
            }
        }
    }

    @Throws(ParseException::class, NumberFormatException::class, Exception::class)
    private fun findMultiplicationAndDivision() {
        operationsList.firstOrNull {char -> char == MULTI || char == DIV }?.let {
            if (it == MULTI){
                val index = operationsList.indexOf(it)
                val results = createResults(index)
                addAction(Multiplication(results.first, results.second), index)
                if (operationsList.isNotEmpty()){
                    findMultiplicationAndDivision()
                }
            }
            if (it == DIV){
                val index = operationsList.indexOf(it)
                val results = createResults(index)
                addAction(Division(results.first, results.second), index)
                if (operationsList.isNotEmpty()){
                    findMultiplicationAndDivision()
                }
            }
        }
    }

    @Throws(ParseException::class, NumberFormatException::class, Exception::class)
    private fun findExponentiation(){
        operationsList.firstOrNull {char -> char == EXP} ?.let {char ->
            val index = operationsList.indexOf(char)
            val results = createResults(index)
            addAction(Exponentiation(results.first, results.second), results.third?.run { this } ?:index)
            if (operationsList.isNotEmpty()){
                findExponentiation()
            }
        }
    }

    @Throws(ParseException::class, NumberFormatException::class, Exception::class)
    private fun createResults(index : Int) : Triple<Result, Result, Int?>{
        if (index + 1 >= dataList.size) throw ParseException(dataList[index + 1], index + 1)
        val strLeft = dataList.removeAt(index)
        val strRight = dataList.removeAt(index)
        if (operationsList.removeAt(index) == EXP && checkBase(strLeft)){
            addMissedMultiplication(strLeft, index)
            return Triple(Appropriation(null), checkString(strRight), index + 1)
        }
        return Triple(checkString(strLeft), checkString(strRight), null)
    }

    private fun checkBase(string : String) : Boolean{
        return string.contains(X, true) && string.length > 1
    }

    @Throws(ParseException::class)
    private fun addMissedMultiplication(string: String, indexMulti: Int) {
        val indexX = string.indexOf(X)
        if (!string.endsWith(X)) throw ParseException(string, indexX)
        var num = string.substring(0, indexX)
        if (num.length == 1 && num.startsWith(MINUS)) num = NEGATIVE
        operationsList.add(indexMulti, MULTI)
        dataList.add(indexMulti, num)
    }

    private fun createMark(cal : Calculatable){
        val  mark = System.identityHashCode(cal).toString().run {
            determineNegativity(this).plus(Y)
        }
        (cal as Markable).setMark(mark)
    }

    private fun addAction(cal : Calculatable, index : Int){
        addAction(cal)
        if (operationsList.isNotEmpty()){
            dataList.add(index, (cal as Markable).leaveMark())
        }
    }

    private fun addAction(cal : Calculatable){
        createMark(cal)
        actionList.add(cal)
    }

    @Throws(NumberFormatException::class, Exception::class)
    private fun checkString(string: String) : Result{
        if (string.endsWith(Y)) return checkMark(string)
        if (string.endsWith(X)) return checkX(string)
        return Appropriation(getNumberFromString(string))
    }

    @Throws(NumberFormatException::class)
    private fun checkX (string: String) : Result{
        if (string.length == 1) return Appropriation(null)
        val number = getNumberFromString(string.substring(INDEX_FIRST_ELEMENT, string.indexOf(X)))
        val multi = Multiplication(Appropriation(number), Appropriation(null))
        addAction(multi)
        return multi
    }

    @Throws(Exception::class)
    private fun checkMark(string: String) : Result {
        return (actionList.find { cal ->
            (cal as Markable).leaveMark() == string
        }?.run { this as Result } ?: throw Exception(string))
    }

    @Throws(NumberFormatException::class)
    private fun getNumberFromString(string: String) : Float{
        return string.toFloat()
    }
}