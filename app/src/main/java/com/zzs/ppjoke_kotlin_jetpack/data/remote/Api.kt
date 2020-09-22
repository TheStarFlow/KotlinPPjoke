package com.zzs.ppjoke_kotlin_jetpack.data.remote

import com.zzs.ppjoke_kotlin_jetpack.model.respmodel.Feed
import com.zzs.ppjoke_kotlin_jetpack.model.respmodel.User
import com.zzs.ppjoke_kotlin_jetpack.ui.home.u
import com.zzs.ppjoke_kotlin_jetpack.ui.login.UserManager
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface Api {

    @GET("feeds/queryHotFeedsList")
    suspend fun fetchFeedCoroutine(
        @Query("feedType") feedType: String = "",
        @Query("userId")  userId: Long = UserManager.get().getUserId(),
        @Query("feedId") feedId: Int = 0,
        @Query("pageCount") pageCount: Int
    ): BaseResp<Feed>

    @GET("feeds/queryHotFeedsList")
    fun fetchFeedCallBack(
        @Query("feedType") feedType: String = "",
        @Query("userId") userId: Long = UserManager.get().getUserId(),
        @Query("feedId") feedId: Int = 0,
        @Query(
            "pageCount"
        ) pageCount: Int
    ): Call<BaseResp<Feed>>

    @GET("user/insert")
    suspend fun insertUser(
        @Query("name") nickName: String,
        @Query("avatar") avatar: String,
        @Query("qqOpenId") openId: String,
        @Query("expires_time") expTime: Long
    ): User

    @GET("ugc/toggleFeedLike")
    suspend fun toggleFeedLike(
        @Query("itemId") itemId: Long,
        @Query("userId") userId: Long = u.get().getUserId()
    ): BaseResponse<JSONObject>

    @GET("ugc/dissFeed")
    suspend fun toggleFeedDiss(
        @Query("itemId") itemId: Long,
        @Query("userId") userId: Long = u.get().getUserId()
    ): BaseResponse<JSONObject>

}