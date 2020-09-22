package com.zzs.ppjoke_kotlin_jetpack.exoplayer

import android.view.ViewGroup

interface IPlayTarget {

    fun owner():ViewGroup

    fun  isPlaying():Boolean

    fun onActive()

    fun inActive()
}