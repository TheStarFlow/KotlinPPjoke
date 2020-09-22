package com.zzs.ppjoke_kotlin_jetpack

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class BaseViewModel:ViewModel() {

    protected val onError = MutableLiveData<String>()

    val expOnError = onError
}