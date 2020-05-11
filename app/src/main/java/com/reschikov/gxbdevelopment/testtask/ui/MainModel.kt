package com.reschikov.gxbdevelopment.testtask.ui

import androidx.lifecycle.*
import com.reschikov.gxbdevelopment.testtask.wolfram.ApiWolfram
import com.reschikov.gxbdevelopment.testtask.wolfram.model.Img
import kotlinx.coroutines.*

class MainModel(private val calculable: Calculable,
                private val derivable: Derivable) : ViewModel(), Computationable {

    private val result = MutableLiveData<Triple<FloatArray, FloatArray, Pair<FloatArray, FloatArray>>?>()
    private val isVisibleProgress = MutableLiveData<Boolean>()
    private val error = MutableLiveData<Throwable?>()
    private val change = MutableLiveData<Boolean>()
    private val graph = MutableLiveData<Img?>()
    private val isLocal = MutableLiveData<Boolean>()

    override fun getError() :  MutableLiveData<Throwable?>  = error
    override fun getResult() : LiveData<Triple<FloatArray, FloatArray, Pair<FloatArray, FloatArray>>?> = result
    override fun getGraph(): LiveData<Img?> = graph
    override fun isVisibleProgressCalculate() :  MutableLiveData<Boolean>  = isVisibleProgress
    override fun getChange(): MutableLiveData<Boolean> = change
    override fun isLocal(): LiveData<Boolean> = isLocal

    override fun takeInput(isApi : Boolean, expression : String, from : String, to: String) {
        isVisibleProgress.value = true
        result.value = null
        graph.value = null
        error.value = null
        isLocal.value = !isApi
        if (isApi) calculateThroughAPI(expression, from, to)
        else calculateLocally(expression, from, to)
    }

    private fun calculateThroughAPI(expression : String, from : String, to: String){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                graph.postValue(derivable.derive(ApiWolfram(expression, from, to)))
                change.postValue(true)
            } catch (e: Exception) {
                error.postValue(e)
                isVisibleProgress.postValue(false)
            }
        }
    }

    private fun calculateLocally(expression : String, from : String, to: String){
        viewModelScope.launch(Dispatchers.Default) {
            try {
                change.postValue(calculable.takeDataAsync(expression, from, to))
            } catch (e: Exception) {
                error.postValue(e)
                isVisibleProgress.postValue(false)
            }
        }
    }

    override fun toScale(axisX: Int, axisY: Int) {
        viewModelScope.launch(Dispatchers.Default) {
            try {
                result.postValue(calculable.reScaleAsync(axisX, axisY))
            } catch (e: Exception) {
                error.postValue(e)
            } finally {
                isVisibleProgress.postValue(false)
            }
        }
    }
}