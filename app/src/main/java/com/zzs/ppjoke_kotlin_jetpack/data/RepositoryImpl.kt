package com.zzs.ppjoke_kotlin_jetpack.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.zzs.network.database.DataBaseManager
import com.zzs.network.service.ApiService
import com.zzs.ppjoke_kotlin_jetpack.data.datasource.FeedDataSource
import com.zzs.ppjoke_kotlin_jetpack.data.factory.FeedDataSourceFactory
import com.zzs.ppjoke_kotlin_jetpack.data.remote.Api
import com.zzs.ppjoke_kotlin_jetpack.data.remote.WebService
import com.zzs.ppjoke_kotlin_jetpack.model.respmodel.Feed
import com.zzs.ppjoke_kotlin_jetpack.model.respmodel.User

class RepositoryImpl private constructor(val webService: WebService, val db: DataBaseManager) :
    Repository {

    var mFeedDataSource:FeedDataSource? = null

    companion object {
        private val instance by lazy {
            RepositoryImpl(webService = WebService(), db = DataBaseManager)
        }

        fun get() = instance
        private val liveDataMap = mutableMapOf<String,LiveData<PagedList<Feed>>>()
    }

    fun getPageData(callBack:PagedList.BoundaryCallback<Feed>,
                    hasData:MutableLiveData<Boolean>,
                    cacheLiveData:MutableLiveData<PagedList<Feed>>,
                    feedType:String): LiveData<PagedList<Feed>> {
        var liveData = liveDataMap[feedType]
        if (liveData==null){
            val config = PagedList.Config.Builder()
                .setPageSize(10)
                .setInitialLoadSizeHint(12)
                .build()
            val factory = FeedDataSourceFactory(webService, db).also {
                it.callBack = object :FeedDataSourceFactory.OnDataSourceCreate{
                    override fun onCreate(dataSource: FeedDataSource) {
                        dataSource.cacheLiveData = cacheLiveData
                        dataSource.pageConfig = config
                        dataSource.hasData = hasData
                        dataSource.feedType = feedType
                        mFeedDataSource = dataSource
                    }
                }
            }
            liveData = LivePagedListBuilder(factory, config)
                .setInitialLoadKey(0)
                .setBoundaryCallback(callBack)
                .build()
            liveDataMap[feedType] = liveData
        }

        return liveData

    }

    suspend fun userInsert(nickName: String, figure2: String, expires_time: Long, openId: String) :User {
        return ApiService.INSTANCE.create(Api::class.java).insertUser(nickName,figure2,openId,expires_time)
    }

}