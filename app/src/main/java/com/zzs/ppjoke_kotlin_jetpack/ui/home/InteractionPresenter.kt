package com.zzs.ppjoke_kotlin_jetpack.ui.home

import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.zzs.common.globals.AppGlobals
import com.zzs.network.service.ApiService
import com.zzs.ppjoke_kotlin_jetpack.data.remote.Api
import com.zzs.ppjoke_kotlin_jetpack.model.respmodel.Feed
import com.zzs.ppjoke_kotlin_jetpack.model.respmodel.User
import com.zzs.ppjoke_kotlin_jetpack.ui.login.UserManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

typealias u = UserManager.Companion
typealias api = ApiService.Companion

object InteractionPresenter {

    @JvmStatic
    fun toggleFeedLike(owner:LifecycleOwner,feed:Feed){
        if (!u.get().isLogin()){
            u.get().login(AppGlobals.application).apply {
                observe(owner, object :Observer<User>{
                    override fun onChanged(t: User?) {
                       this@apply.removeObserver(this)
                        t?.let {
                            toggleFeedLikeInternal(feed)
                        }
                    }
                })
            }
            return
        }
        toggleFeedLikeInternal(feed)

    }

    private fun toggleFeedLikeInternal(feed: Feed) {
        GlobalScope.launch {
            try {
                val jsonObject = api.INSTANCE.create(Api::class.java).toggleFeedLike(itemId = feed.itemId)
                val hasLike = jsonObject.data.data.getBoolean("hasLiked")
//            feed.ugc.hasLiked = hasLike
            }catch (e:Exception){
                toast(e.toString())
            }

        }

    }

    private suspend fun toast(content:String) = withContext(Dispatchers.Main){
        Toast.makeText(AppGlobals.application,content,Toast.LENGTH_SHORT).show()
    }

    @JvmStatic
    fun toggleFeedDiss(feed: Feed,owner: LifecycleOwner){
        if (!u.get().isLogin()){
            u.get().login(AppGlobals.application).apply {
                observe(owner, object :Observer<User>{
                    override fun onChanged(t: User?) {
                        this@apply.removeObserver(this)
                        t?.also {
                            toggleFeedDissInternal(feed)
                        }
                    }
                })
            }
            return
        }
        toggleFeedDissInternal(feed)
    }

    private fun toggleFeedDissInternal(feed: Feed){
        GlobalScope.launch {
            try {
                val jsonObject = api.INSTANCE.create(Api::class.java).toggleFeedDiss(itemId = feed.itemId)
                val hasLike = jsonObject.data.data.getBoolean("hasLiked")
//            feed.ugc.hasLiked = hasLike
            }catch (e:Exception){
                toast(e.toString())
            }
        }
    }



}