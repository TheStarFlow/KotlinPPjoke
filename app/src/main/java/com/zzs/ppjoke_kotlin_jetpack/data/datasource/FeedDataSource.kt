package com.zzs.ppjoke_kotlin_jetpack.data.datasource

import androidx.lifecycle.MutableLiveData
import androidx.paging.ItemKeyedDataSource
import androidx.paging.PagedList
import com.zzs.network.database.DataBaseManager
import com.zzs.ppjoke_kotlin_jetpack.common.ext.logWithD
import com.zzs.ppjoke_kotlin_jetpack.common.ext.logWithE
import com.zzs.ppjoke_kotlin_jetpack.data.remote.BaseResp
import com.zzs.ppjoke_kotlin_jetpack.data.remote.WebService
import com.zzs.ppjoke_kotlin_jetpack.model.respmodel.Feed
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.Serializable
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean

class FeedDataSource(private val webService: WebService, private val db: DataBaseManager) :
    ItemKeyedDataSource<Int, Feed>() {

    var pageConfig :PagedList.Config ? = null

    val TAG = javaClass.simpleName

    var widthCache :Boolean = true

    var cacheLiveData:MutableLiveData<PagedList<Feed>>? = null

    var hasData:MutableLiveData<Boolean>? = null

    var isLoadAfter = AtomicBoolean(false)

    var feedType = "all"

    override fun getKey(item: Feed): Int {
        return item.id.toInt()
    }

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Feed>) {
        loadDataCallBack(callback, 0)
        //已经运行在IO线程，不需要额外切线程去加载数据，否则会引起LiveData在线程完成后反馈空数据，
        // 真正获取的数据无法再次被反应到UI
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Feed>) {
        loadDataCallBack(callback, params.key)
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Feed>) {
        callback.onResult(Collections.emptyList())
    }

    //使用协程会另起线程去获取数据  在这里不需要 否则会出现返回的livedata因为被订阅而先返回空数据，导致真实获取
    //的数据再次调用callBACK.onResult时无效
    fun loadDataCoroutine(callback: LoadCallback<Feed>, key: Int) {
        //协程全局作用域。尽量少用，作用域为整个App生命周期
        GlobalScope.launch(Dispatchers.Unconfined) {
            try {
                isLoadAfter.set(key>0)
                if (widthCache){
                    db.getCache(Feed.CACHE_KEY+feedType)?.apply {
                        val dataSource = MutableDataSource<Int,Feed>()
                        dataSource.dataList.addAll(this as List<Feed>)
                        val pagedList = dataSource.getPageList(pageConfig!!)
                        cacheLiveData?.postValue(pagedList)
                        "获取缓存成功".logWithD(TAG)
//                        callback.onResult(this as List<Feed>)
                    }
                    widthCache = false
                }
                "加载网络数据,key = ${key}".logWithD(TAG)
                val resp = webService.fetchFeedCoroutine(pageCount = 10, feedId = key,feedType = feedType)
                val data = resp.data.data.toMutableList()
                callback.onResult(data)
                if (key==0&& data is Serializable){
                    "获取网络数据成功,存入缓存数据库".logWithD(TAG)
                    db.insert(Feed.CACHE_KEY+feedType,data)
                }
                isLoadAfter.set(false)
                hasData?.postValue(data.isNotEmpty())
            }catch (e:Exception){
                "获取Feed数据出现异常${e.toString()}".logWithE(TAG)
                e.printStackTrace()
            }finally {
                isLoadAfter.set(false)
                hasData?.postValue(false)
            }

        }
    }

    //主线程调用数据
    fun loadDataCallBack(callback: LoadCallback<Feed>, key: Int){
        try {
            isLoadAfter.set(key>0)
            if (widthCache){
                db.getCache(Feed.CACHE_KEY+feedType)?.apply {
                    val dataSource = MutableDataSource<Int,Feed>()
                    dataSource.dataList.addAll(this as List<Feed>)
                    val pagedList = dataSource.getPageList(pageConfig!!)
                    cacheLiveData?.postValue(pagedList)
                    "获取缓存成功".logWithD(TAG)
//                        callback.onResult(this as List<Feed>)
                }
                widthCache = false
            }
            "加载网络数据,key = ${key}".logWithD(TAG)
            val resp = webService.fetchFeedCallBack(pageCount = 10, feedId = key,feedType = feedType )
            val response = resp.execute()
            if(response.isSuccessful){
                val list = response.body()?.data?.data
                callback.onResult(list!!)
                if (key==0&& list is Serializable){
                    "获取网络数据成功,存入缓存数据库".logWithD(TAG)
                    db.insert(Feed.CACHE_KEY+feedType,list)
                }
                isLoadAfter.set(false)
                hasData?.postValue(list.isNotEmpty())
            }
        }catch (e:Exception){
            "获取Feed数据出现异常${e.toString()}".logWithE(TAG)
            e.printStackTrace()
        }finally {
            isLoadAfter.set(false)
            hasData?.postValue(false)
        }
    }
}