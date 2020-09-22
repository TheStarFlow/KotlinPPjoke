package com.zzs.ppjoke_kotlin_jetpack.common.utils

import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.WindowManager

class StatusBar {
    companion object {
        fun fisSystemBar(aty: Activity) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return
            aty.window.apply {
                addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                statusBarColor  = Color.TRANSPARENT
            }.decorView.apply {
                //View.SYSTEM_UI_FLAG_FULLSCREEN = 4.4系统 WindowManager.LayoutParams.FLAG_FULLSCREEN 会隐藏状态栏
                //布局延伸至状态栏  但是不隐藏
                systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE or  //稳定的布局。不管导航栏可见与否都不影响 布局是否在状态栏之下
                        View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR //让状态栏文字自适应颜色  变成白底黑字
            }

        }
    }
}