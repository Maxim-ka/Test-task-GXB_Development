package com.reschikov.gxbdevelopment.testtask.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.reschikov.gxbdevelopment.testtask.wolfram.model.Img

interface Computationable {

    fun takeInput(isApi : Boolean, expression : String, from : String, to : String)
    fun getResult() : LiveData<Triple<FloatArray, FloatArray, Pair<FloatArray, FloatArray>>?>
    fun getGraph() : LiveData<Img?>
    fun toScale(axisX : Int, axisY : Int)
    fun isVisibleProgressCalculate() :  MutableLiveData<Boolean>
    fun getError() :  MutableLiveData<Throwable?>
    fun getChange() : MutableLiveData<Boolean>
    fun isLocal() : LiveData<Boolean>
}