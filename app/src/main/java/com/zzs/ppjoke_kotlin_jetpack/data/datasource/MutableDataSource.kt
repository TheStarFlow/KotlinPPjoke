package com.zzs.ppjoke_kotlin_jetpack.data.datasource

import android.annotation.SuppressLint
import androidx.arch.core.executor.ArchTaskExecutor
import androidx.paging.LivePagedListBuilder
import androidx.paging.PageKeyedDataSource
import androidx.paging.PagedList
import java.util.*

class MutableDataSource<Key,Value> :PageKeyedDataSource<Key,Value>() {
    val dataList = mutableListOf<Value>()


    @SuppressLint("RestrictedApi")
    fun getPageList(config: PagedList.Config):PagedList<Value>{
        return PagedList.Builder<Key,Value>(this,config).apply {
            setFetchExecutor(ArchTaskExecutor.getIOThreadExecutor())
            setNotifyExecutor(ArchTaskExecutor.getMainThreadExecutor())
        }.build()
    }

    override fun loadInitial(

        params: LoadInitialParams<Key>,
        callback: LoadInitialCallback<Key, Value>
    ) {
        callback.onResult(dataList,null,null)
    }

    override fun loadBefore(params: LoadParams<Key>, callback: LoadCallback<Key, Value>) {
        callback.onResult(Collections.emptyList(),null)
    }

    override fun loadAfter(params: LoadParams<Key>, callback: LoadCallback<Key, Value>) {
        callback.onResult(Collections.emptyList(),null)
    }
}