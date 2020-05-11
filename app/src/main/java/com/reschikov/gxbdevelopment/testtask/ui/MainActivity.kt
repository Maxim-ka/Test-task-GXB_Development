package com.reschikov.gxbdevelopment.testtask.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.reschikov.gxbdevelopment.testtask.R

import com.reschikov.gxbdevelopment.testtask.ui.dialogs.showAlertDialog
import com.reschikov.gxbdevelopment.testtask.ui.input.InputFragment
import com.reschikov.gxbdevelopment.testtask.ui.plotting.GraphFragment
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.lang.ArithmeticException

private const val TAG_GRAPH = "Tag graph"
private const val TAG_INPUT = "Tag input"

class MainActivity : AppCompatActivity(){

    private val computationable : Computationable by viewModel<MainModel>()
    private val observerChange by lazy {
        Observer<Boolean> {
            if (it) {
                closeKeyBoard()
                loadFragment(GraphFragment(), TAG_GRAPH)
                computationable.getChange().value = false
            }
        }
    }
    private val observerVisibilityProcess by lazy {
        Observer<Boolean> { pb_circle.visibility = if(it) View.VISIBLE else View.GONE }
    }
    private val observerError by lazy {
        Observer<Throwable?> { it?.let { renderError(it) } }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        computationable.getChange().observe(this, observerChange)
        computationable.isVisibleProgressCalculate().observe(this, observerVisibilityProcess)
        computationable.getError().observe(this, observerError)
        if (savedInstanceState == null) {
            loadFragment(InputFragment(), TAG_INPUT)
            showAlertDialog(getString(R.string.attention), getString(R.string.check_function))
        }
    }

    private fun renderError(e : Throwable){
        val message : String = when(e){
            is ArithmeticException ->  getString(R.string.err_function_no_solution)
            else -> getString(R.string.check_function)
        }
        showAlertDialog(e.javaClass.simpleName , "${getString(R.string.err_error)} < ${e.message} >\n$message")
    }

    private fun loadFragment(fragment: Fragment, tag: String){
        supportFragmentManager.beginTransaction()
            .add(R.id.frame_master, fragment, tag)
            .addToBackStack(tag)
            .commit()
    }

    private fun closeKeyBoard() {
        currentFocus?.let {
            val imm =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(it.windowToken, 0)
            it.clearFocus()
        }
    }

    override fun onBackPressed() {
        if (supportFragmentManager.fragments.size == 1 &&
            supportFragmentManager.fragments.first().tag == TAG_INPUT) finish()
        super.onBackPressed()
    }
}
