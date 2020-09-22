package com.zzs.ppjoke_kotlin_jetpack.common.utils

object StringConvert {

    @JvmStatic
    fun stringConvert(count:Int):String{
        return if (count<10000) count.toString()
        else
            "${count/10000}万"
    }
}