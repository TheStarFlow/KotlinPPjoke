package com.zzs.ppjoke_kotlin_jetpack.exoplayer

import android.net.Uri
import android.webkit.URLUtil
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.upstream.FileDataSourceFactory
import com.google.android.exoplayer2.upstream.cache.*
import com.google.android.exoplayer2.util.Util
import com.zzs.common.globals.AppGlobals

object PageListManager {

    private val mediaSource:ProgressiveMediaSource.Factory

    init {
        val app = AppGlobals.application
        val dataSourceFactory = DefaultHttpDataSourceFactory(Util.getUserAgent(app,app.packageName))
        val cache = SimpleCache(app.cacheDir,LeastRecentlyUsedCacheEvictor(1024*1024*200))
        val cacheDataSinkFactory = CacheDataSinkFactory(cache, Long.MAX_VALUE)
        val dataSource = CacheDataSourceFactory(
            cache,dataSourceFactory,FileDataSourceFactory() ,cacheDataSinkFactory,CacheDataSource.FLAG_BLOCK_ON_CACHE,null)
        mediaSource = ProgressiveMediaSource.Factory(dataSource)
    }

    fun createDataSource(uri:String) = mediaSource.createMediaSource(Uri.parse(uri))


    private val pageListHashMap = mutableMapOf<String, PageListPlay>()


    fun get(pageName: String): PageListPlay? {
        return if (pageListHashMap[pageName] != null&& pageListHashMap[pageName]?.mPlayerView!=null) {
            pageListHashMap[pageName]!!
        } else {
            PageListPlay().also { pageListHashMap[pageName] = it }
        }
    }

    fun release(pageName: String){
        pageListHashMap[pageName]?.also {
            it.release()
        }
    }

}