package com.zzs.ppjoke_kotlin_jetpack.data.remote

import com.zzs.network.service.ApiService
import com.zzs.ppjoke_kotlin_jetpack.model.respmodel.Feed
import retrofit2.Call

class WebService {


     suspend fun fetchFeedCoroutine(
        feedType: String = "",
        userId: Int = 0,
        feedId: Int = 0,
        pageCount: Int
    ): BaseResp<Feed>
            =  ApiService.INSTANCE.create(Api::class.java).
                fetchFeedCoroutine(feedType, userId.toLong(), feedId, pageCount)

    fun fetchFeedCallBack(
        feedType: String = "",
        userId: Int = 0,
        feedId: Int = 0,
        pageCount: Int
    ):Call<BaseResp<Feed>> = ApiService.INSTANCE.create(Api::class.java).
    fetchFeedCallBack(feedType, userId.toLong(), feedId, pageCount)

}