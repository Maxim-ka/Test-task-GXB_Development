package com.reschikov.gxbdevelopment.testtask.ui.plotting

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import com.reschikov.gxbdevelopment.testtask.INDEX_FIRST_ELEMENT

private const val PEN_WIDTH = 4.0f
private const val HALF = 0.5f
private const val TEXT_SIZE = 32.0f

class FunctionGraph : androidx.appcompat.widget.AppCompatImageView {

    constructor(context: Context?) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val graph = Paint()
    private val point = Paint()
    private val axis = Paint()
    private val path = Path()
    private var arrPoints : FloatArray? = null
    private var arrAxisX : FloatArray? = null
    private var arrAxisY : FloatArray? = null
    private var holder : CharSequence? = null
    private lateinit var beginningHolder : Pair<Float, Float>

    init {
        graph.color = Color.BLUE
        graph.strokeWidth = PEN_WIDTH
        graph.style = Paint.Style.STROKE
        graph.strokeJoin = Paint.Join.ROUND
        point.color = Color.RED
        point.strokeWidth = PEN_WIDTH * 2
        point.strokeCap = Paint.Cap.ROUND
        point.isAntiAlias = true
        axis.color = Color.BLACK
        axis.strokeWidth = PEN_WIDTH * HALF
        axis.textSize = TEXT_SIZE
    }

    fun setData(data : Triple<FloatArray, FloatArray, Pair<FloatArray, FloatArray>>){
        arrAxisX = data.first
        arrAxisY = data.second
        val arrayX = data.third.first
        val arrayY = data.third.second
        buildFunction(arrayX, arrayY)
        createPoints(arrayX, arrayY)
        invalidate()
    }

    private fun buildFunction(arrayX : FloatArray, arrayY: FloatArray){
        path.reset()
        arrayX.forEachIndexed { index, x ->
            if (index == 0) path.moveTo(x, arrayY[index])
            else path.lineTo(x, arrayY[index])
        }
    }

    private fun createPoints(arrayX : FloatArray, arrayY: FloatArray){
        arrPoints = FloatArray(arrayX.size + arrayY.size)
        var index = 0
        arrPoints?.let {points ->
            repeat(arrayX.size){
                points[index++] = arrayX[it]
                points[index++] = arrayY[it]
            }
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        holder?.let { canvas.drawText(it, INDEX_FIRST_ELEMENT, it.length, beginningHolder.first, beginningHolder.second, axis) }
        arrAxisX?.let { canvas.drawLines(it, axis) }
        arrAxisY?.let { canvas.drawLines(it, axis) }
        canvas.drawPath(path, graph)
        arrPoints?.let { canvas.drawPoints(it, point) }
    }

    override fun setContentDescription(contentDescription: CharSequence?) {
        super.setContentDescription(contentDescription)
        holder = contentDescription
        createBeginningHolder()
    }

    private fun createBeginningHolder(){
        holder?.let {
            val widthHolder = axis.measureText(holder, INDEX_FIRST_ELEMENT, it.length)
            beginningHolder = Pair(rootView.width * HALF - widthHolder * HALF, rootView.height * HALF)
        }
    }
}