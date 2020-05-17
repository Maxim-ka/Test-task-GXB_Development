package com.reschikov.gxbdevelopment.testtask.ui.input

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.reschikov.gxbdevelopment.testtask.R
import com.reschikov.gxbdevelopment.testtask.network.CheckNetWork
import com.reschikov.gxbdevelopment.testtask.ui.Computationable
import com.reschikov.gxbdevelopment.testtask.ui.MainModel
import kotlinx.android.synthetic.main.block_range.*
import kotlinx.android.synthetic.main.fragment_input.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class InputFragment : Fragment(R.layout.fragment_input) {

    private val computationable : Computationable by sharedViewModel<MainModel>()
    private val listenerCalculate : View.OnClickListener by lazy {
        View.OnClickListener {
            val expression = checkTextField(tiet_expression, til_expression)
            val from = checkTextField(tiet_from, til_from)
            val to = checkTextField(tiet_to, til_to)
            if (sw_api.isChecked && isNoNet()) return@OnClickListener
            if (expression != null && from != null && to != null){
                computationable.takeInput(sw_api.isChecked, expression, from, to)
            }
        }
    }
    private val observerNet by lazy {
        Observer<Boolean>{  hasNet = it }
    }
    private var hasNet : Boolean = false
    private lateinit var checkNetWork: LiveData<Boolean>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkNetWork = CheckNetWork(view.context)
        checkNetWork.observe(this, observerNet)
    }

    private fun isNoNet() : Boolean {
        return if (hasNet) false
        else{
            sw_api.isChecked = false
            computationable.getError().value = Exception(getString(R.string.err_no_network))
            true
        }
    }

    private fun checkTextField(tiet : TextInputEditText, til : TextInputLayout) : String?{
        if (tiet.text.isNullOrEmpty() || tiet.text.isNullOrBlank()) {
            til.error = getString(R.string.err_not_filled)
            return null
        }
        til.error = null
        return tiet.text.toString()
    }

    override fun onStart() {
        super.onStart()
        but_calculate.setOnClickListener(listenerCalculate)
    }

    override fun onStop() {
        super.onStop()
        but_calculate.setOnClickListener(null)
    }
}