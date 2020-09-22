package com.zzs.common.globals

import android.annotation.SuppressLint
import android.app.Application

class AppGlobals {
    @SuppressLint("DiscouragedPrivateApi")
    companion object {
        val application by lazy {
            val method =
                Class.forName("android.app.ActivityThread").getDeclaredMethod("currentApplication")
            method.invoke(null) as Application

        }
    }
}