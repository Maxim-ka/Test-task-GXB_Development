package com.reschikov.gxbdevelopment.testtask.wolfram

import com.reschikov.gxbdevelopment.testtask.wolfram.model.Img
import com.reschikov.gxbdevelopment.testtask.wolfram.model.Queryresult
import com.reschikov.gxbdevelopment.testtask.ui.Derivable
import kotlin.coroutines.suspendCoroutine

private const val APP_ID = "QEJ9GH-JK4JGYV62E"
private const val INCLUDE_POD_ID = "Plot"
private const val LACK_OF_RESULT = "img == null"
private const val SUCCESS_OF_RESULT = "result.success"

class ApiWolfram(private val expression: String,
                 private val from: String,
                 private  val to: String) : Requestable<Img>{

    @Throws(Exception::class, Throwable::class)
    override suspend fun executeRequest(derivable: Derivable): Img {
        val request = derivable.getRetrofitable().getRetrofit().create(WolframAlpha::class.java)
        val result = derivable.getResult(request)
        var msg : String? = null
        if (result.hasError || !result.success) {
            msg = result.error?.let { "${it.code} ${it.msg}" } ?: "$SUCCESS_OF_RESULT=${result.success}"
        } else {
            result.pods?.forEach {pod ->
                if (!pod.hasError) {
                    pod.subpods?.forEach {sub -> sub.img?.let { return it } }
                } else {
                    pod.error?.let { err-> msg = "${err.code} ${err.msg}" }
                }
            }
        }
        msg ?: run{ msg = LACK_OF_RESULT }
        throw Exception(msg)
    }

    @Throws(Exception::class)
    private suspend fun Derivable.getResult(request : WolframAlpha) : Queryresult {
        val input = "plot $expression from x=$from to $to"
        return suspendCoroutine { continuation ->
            request.getGraphFunction(APP_ID, input, INCLUDE_POD_ID)
                .enqueue(getCallBack<Queryresult>(continuation))
        }
    }
}