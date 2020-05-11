package com.reschikov.gxbdevelopment.testtask.ui.plotting

import android.content.res.Configuration
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.view.ViewTreeObserver
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.reschikov.gxbdevelopment.testtask.R
import com.reschikov.gxbdevelopment.testtask.wolfram.model.Img
import com.reschikov.gxbdevelopment.testtask.ui.Computationable
import com.reschikov.gxbdevelopment.testtask.ui.MainModel
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_graph.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import kotlin.Exception

private const val WAITING_TIME = 15_000L
private const val INTERVAL = 20_000L

class GraphFragment : Fragment(R.layout.fragment_graph) {

    private val computationable : Computationable by sharedViewModel<MainModel>()
    private val observerResult by lazy {
        Observer<Triple<FloatArray, FloatArray, Pair<FloatArray, FloatArray>>?> {
            it?.let { custom_graph.setData(it) }
        }
    }
    private val observerGraph by lazy {
        Observer<Img?> {
            it?.let { loadImageGraph(it) }
        }
    }
    private val observerLocal by lazy {
        Observer<Boolean> { if (it) { view?.let {view ->
            view.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener{
                override fun onGlobalLayout() {
                    view.viewTreeObserver?.removeOnGlobalLayoutListener(this)
                    val size : Int = if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) view.width
                    else view.height
                    computationable.toScale(size, size)
                }
            })
        } } }
    }
    private var countDownTimer: CountDownTimer? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        computationable.getResult().observe(this, observerResult)
        computationable.getGraph().observe(this, observerGraph)
        computationable.isLocal().observe(this, observerLocal)
    }

    private fun loadImageGraph(img: Img){
        custom_graph.contentDescription = img.alt
        startCountdown()
        Picasso.get().load(img.src).into(custom_graph, object : Callback{
            override fun onSuccess() {
                stopCountdown()
                custom_graph.contentDescription = null
                computationable.isVisibleProgressCalculate().value = false
            }

            override fun onError(e: Exception?) {
                stopCountdown()
                createMessageError(e)
            }
        })
    }

    private fun startCountdown(){
        if (countDownTimer == null){
            countDownTimer = object : CountDownTimer(WAITING_TIME, INTERVAL) {
                override fun onFinish() {
                    Picasso.get().cancelRequest(custom_graph)
                    createMessageError(Exception(getString(R.string.err_timed_out)))
                }

                override fun onTick(millisUntilFinished: Long) {}
            }.start()
        }
    }

    private fun stopCountdown(){
        countDownTimer?.cancel()
        countDownTimer = null
    }

    private fun createMessageError(e : Exception?){
        computationable.getError().value = e
        computationable.isVisibleProgressCalculate().value = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        stopCountdown()
    }
}