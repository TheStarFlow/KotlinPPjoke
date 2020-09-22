package com.zzs.ppjoke_kotlin_jetpack.exoplayer

import android.view.LayoutInflater
import com.google.android.exoplayer2.DefaultLoadControl
import com.google.android.exoplayer2.DefaultRenderersFactory
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.PlayerControlView
import com.google.android.exoplayer2.ui.PlayerView
import com.zzs.common.globals.AppGlobals
import com.zzs.ppjoke_kotlin_jetpack.R

class PageListPlay() {


    var mSimpleExoPlayer: SimpleExoPlayer?
    var mPlayerView:PlayerView?
    val contolView:PlayerControlView?
    var url = ""


    init {
        val app = AppGlobals.application
        mSimpleExoPlayer = ExoPlayerFactory.newSimpleInstance(app,
        DefaultRenderersFactory(app),DefaultTrackSelector(),DefaultLoadControl())
        mPlayerView = LayoutInflater.from(app).inflate(R.layout.layout_exo_player_view,null,false) as PlayerView
        contolView = LayoutInflater.from(app).inflate(R.layout.layout_simple_exo_controller_view,null,false) as PlayerControlView
        mPlayerView?.player = mSimpleExoPlayer
        contolView.player = mSimpleExoPlayer

    }

    fun release() {
        mSimpleExoPlayer?.apply {
            playWhenReady = false
            stop(true)
            release()
            mSimpleExoPlayer = null
        }
        mPlayerView?.apply {
            player = null
            mPlayerView = null
        }
        contolView?.apply {
           player = null
        }

    }

    fun switchPlayerView(newPlayerView: PlayerView, attach: Boolean) {
        mPlayerView?.player = if (attach) null else mSimpleExoPlayer
        newPlayerView.player = if (attach) mSimpleExoPlayer else null
    }

}