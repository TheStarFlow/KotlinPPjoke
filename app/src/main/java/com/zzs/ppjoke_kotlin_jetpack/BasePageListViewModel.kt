package com.zzs.ppjoke_kotlin_jetpack

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.zzs.ppjoke_kotlin_jetpack.model.respmodel.Feed

abstract class BasePageListViewModel<T> : BaseViewModel() {

    protected open lateinit var pageData: LiveData<PagedList<T>>

    protected val hasData = MutableLiveData<Boolean>()
    val expHasData: LiveData<Boolean> = hasData


    inner class CallBack : PagedList.BoundaryCallback<Feed>() {

        override fun onZeroItemsLoaded() {
            hasData.value = false
        }

        override fun onItemAtFrontLoaded(itemAtFront: Feed) {
            hasData.value = true
        }

        override fun onItemAtEndLoaded(itemAtEnd: Feed) {
            super.onItemAtEndLoaded(itemAtEnd)
        }
    }

    fun fetchPageData(): LiveData<PagedList<T>>{
        if (!this::pageData.isInitialized){
            pageData = initPageData()
        }
        return pageData
    }

    abstract fun initPageData(): LiveData<PagedList<T>>
}