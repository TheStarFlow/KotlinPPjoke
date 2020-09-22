package com.zzs.ppjoke_kotlin_jetpack.view

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class MIUILoading @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    val mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    val circleColor  = Color.BLUE
    val circleRadius = 256f
    val anim = ObjectAnimator.ofFloat()

    init {
        mPaint.apply {
            color = circleColor
            strokeWidth = 12f
            style = Paint.Style.STROKE
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.apply {
            save()
            translate(width/2f,height/2f)
            drawCircle(0f,0f,circleRadius,mPaint)
            restore()
        }
    }
}