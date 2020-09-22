package com.zzs.ppjoke_kotlin_jetpack.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.ItemKeyedDataSource
import androidx.paging.PagedList
import com.zzs.ppjoke_kotlin_jetpack.BasePageListViewModel
import com.zzs.ppjoke_kotlin_jetpack.data.RepositoryImpl
import com.zzs.ppjoke_kotlin_jetpack.data.datasource.FeedDataSource
import com.zzs.ppjoke_kotlin_jetpack.model.respmodel.Feed
import java.util.*

class HomeViewModel : BasePageListViewModel<Feed>() {

    private val repository by lazy { RepositoryImpl.get() }

    val cacheFeedLiveData = MutableLiveData<PagedList<Feed>>()

    var feedType = "all"


    fun getFeedDataSource(): FeedDataSource? = repository.mFeedDataSource

    fun loadMore(callback: ItemKeyedDataSource.LoadCallback<Feed>, key: Long) {
        var feedDataSource = getFeedDataSource() ?: return
        if (feedDataSource.isLoadAfter.get()) { //如果正在请求分页,手动请求的就停止
            callback.onResult(Collections.emptyList())
            return
        }
        getFeedDataSource()?.loadDataCoroutine(callback, key.toInt())
    }

    override fun initPageData(): LiveData<PagedList<Feed>> {
        return repository.getPageData(CallBack(), hasData, cacheFeedLiveData, feedType)
    }


}