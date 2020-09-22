package com.zzs.common.utils

import android.util.DisplayMetrics
import com.zzs.common.globals.AppGlobals

object PixUtils {
    fun dp2px(dpValue: Int): Int {
        val metrics: DisplayMetrics = AppGlobals.application.resources.displayMetrics
        return (metrics.density * dpValue + 0.5f).toInt()
    }

    fun getScreenWidth(): Int {
        val metrics: DisplayMetrics = AppGlobals.application.resources.displayMetrics
        return metrics.widthPixels
    }

    fun getScreenHeight(): Int {
        val metrics: DisplayMetrics = AppGlobals.application.resources.displayMetrics
        return metrics.heightPixels
    }
}