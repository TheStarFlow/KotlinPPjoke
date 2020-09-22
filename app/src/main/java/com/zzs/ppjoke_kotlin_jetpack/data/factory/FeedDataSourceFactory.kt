package com.zzs.ppjoke_kotlin_jetpack.data.factory

import androidx.paging.DataSource
import androidx.paging.ItemKeyedDataSource
import com.zzs.network.database.DataBaseManager
import com.zzs.ppjoke_kotlin_jetpack.data.datasource.FeedDataSource
import com.zzs.ppjoke_kotlin_jetpack.data.remote.WebService
import com.zzs.ppjoke_kotlin_jetpack.model.respmodel.Feed

class FeedDataSourceFactory(private val webService: WebService, private val db: DataBaseManager) :
    DataSource.Factory<Int, Feed>() {

    lateinit var mDataSource: FeedDataSource
    var callBack: OnDataSourceCreate? = null
    var withCache = true

    override fun create(): DataSource<Int, Feed> {
        if (!this::mDataSource.isInitialized||mDataSource.isInvalid){
            mDataSource = FeedDataSource(webService, db)
            mDataSource.widthCache = withCache
            callBack?.onCreate(mDataSource)
        }
        withCache = false
        return mDataSource;
    }

    interface OnDataSourceCreate {
        fun onCreate(dataSource: FeedDataSource)
    }
}