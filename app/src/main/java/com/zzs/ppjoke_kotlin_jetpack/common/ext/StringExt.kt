package com.zzs.ppjoke_kotlin_jetpack.common.ext

import android.content.Context
import android.util.Log
import android.widget.Toast


fun String.logWithE(tag:String){
    Log.e(tag,this)
}

fun String.logWithD(tag:String){
    Log.d(tag,this)
}

fun String.toast(context: Context){
    Toast.makeText(context,this,Toast.LENGTH_SHORT).show()
}