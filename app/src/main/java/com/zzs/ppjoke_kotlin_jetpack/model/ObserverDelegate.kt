package com.zzs.ppjoke_kotlin_jetpack.model

import androidx.databinding.BaseObservable
import com.zzs.ppjoke_kotlin_jetpack.model.respmodel.Ugc
import kotlin.reflect.KProperty

class ObserverDelegate<T>(private val baseObservable: BaseObservable, val id:Int){

    operator fun getValue(thisRef: T, property: KProperty<*>): T {
        return thisRef
    }

    operator fun setValue(thisRef: T, property: KProperty<*>, value: T) {
        baseObservable.notifyPropertyChanged(id)
    }

}