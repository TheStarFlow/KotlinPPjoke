package com.zzs.ppjoke_kotlin_jetpack.view

import android.content.Context
import android.util.AttributeSet
import android.view.*
import android.widget.FrameLayout
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.ui.PlayerControlView
import com.google.android.exoplayer2.ui.PlayerView
import com.zzs.common.utils.PixUtils
import com.zzs.ppjoke_kotlin_jetpack.R
import com.zzs.ppjoke_kotlin_jetpack.common.ext.loadImage
import com.zzs.ppjoke_kotlin_jetpack.common.ext.onClick
import com.zzs.ppjoke_kotlin_jetpack.common.ext.setBlurImage
import com.zzs.ppjoke_kotlin_jetpack.exoplayer.IPlayTarget
import com.zzs.ppjoke_kotlin_jetpack.exoplayer.PageListManager
import com.zzs.ppjoke_kotlin_jetpack.exoplayer.PageListPlay
import kotlinx.android.synthetic.main.layout_player_view.view.*

class ListPlayView : FrameLayout, IPlayTarget, PlayerControlView.VisibilityListener,
    Player.EventListener {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        LayoutInflater.from(context).inflate(R.layout.layout_player_view, this, true)


        mPlayBtn.onClick {
            if (playing) inActive() else onActive()
        }
    }

    var videoUrl: String = ""
    var category: String = ""

    var playing = false


    fun bindData(category: String, wPx: Int, hPx: Int, coverUrl: String, videoUrl: String) {
        this.videoUrl = videoUrl
        this.category = category
        mCoverImg.loadImage(coverUrl)
        if (wPx < hPx) {
            mBlueBackground.setBlurImage(coverUrl, 10f)
            mBlueBackground.visibility = View.VISIBLE
        } else {
            mBlueBackground.visibility = INVISIBLE
        }
        setSize(wPx, hPx)
    }

    protected fun setSize(widthPx: Int, heightPx: Int) {
        //这里主要是做视频宽大与高,或者高大于宽时  视频的等比缩放
        val maxWidth = PixUtils.getScreenWidth()
        var layoutHeight = maxWidth
        val coverWidth: Int
        val coverHeight: Int
        if (widthPx >= heightPx) {
            coverWidth = maxWidth
            coverHeight = (heightPx / (widthPx * 1.0f / maxWidth)).toInt()
            layoutHeight = coverHeight
        } else {
            coverHeight = maxWidth
            layoutHeight = coverHeight
            coverWidth = (widthPx / (heightPx * 1.0f / maxWidth)).toInt()
        }
        val params = layoutParams
        params.width = maxWidth
        params.height = layoutHeight
        layoutParams = params
        val blurParams: ViewGroup.LayoutParams = mBlueBackground.layoutParams
        blurParams.width = maxWidth
        blurParams.height = layoutHeight
        mBlueBackground.layoutParams = blurParams
        val coverParams = mCoverImg.layoutParams as LayoutParams
        coverParams.width = coverWidth
        coverParams.height = coverHeight
        coverParams.gravity = Gravity.CENTER
        mCoverImg.layoutParams = coverParams
        val playBtnParams = mPlayBtn.layoutParams as LayoutParams
        playBtnParams.gravity = Gravity.CENTER
        mPlayBtn.layoutParams = playBtnParams
    }

    override fun owner() = this

    override fun isPlaying() = playing

    override fun onActive() {
        val pageListPlay = PageListManager.get(category)?:return
        pageListPlay.mPlayerView?:return
        pageListPlay.switchPlayerView(pageListPlay.mPlayerView!!, true)
        val playerView: PlayerView? = pageListPlay.mPlayerView
        val controlView: PlayerControlView? = pageListPlay.contolView
        val exoPlayer: SimpleExoPlayer? = pageListPlay.mSimpleExoPlayer
        val parent = playerView?.parent
        if (parent != this) {
            if (parent != null) {
                (parent as ViewGroup).removeView(playerView)
                (parent as ListPlayView).inActive()
            }
            val coverParams: ViewGroup.LayoutParams = mCoverImg.layoutParams
            this@ListPlayView.addView(playerView, 1, coverParams)
        }
        if (controlView?.parent!=this){
            controlView?.parent?.also {
                (it as ViewGroup).removeView(controlView)
            }
            addView(controlView, LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).also {
                it.gravity = Gravity.BOTTOM
            })

        }
//        controlView?.parent?.apply {
//            if (this != this@ListPlayView) {
//                (this as ViewGroup).removeView(controlView)
//            }
//            addView(controlView, LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.WRAP_CONTENT
//            ).also {
//                it.gravity = Gravity.BOTTOM
//            })
//        }
        if (pageListPlay.url == videoUrl) {
            onPlayerStateChanged(true, Player.STATE_READY)
        } else {
            val mediaSource: MediaSource = PageListManager.createDataSource(videoUrl)
            exoPlayer?.prepare(mediaSource)
            exoPlayer?.repeatMode = Player.REPEAT_MODE_ONE
            pageListPlay.url = videoUrl

        }
        controlView!!.show()
        controlView.addVisibilityListener(this)
        exoPlayer?.addListener(this)
        exoPlayer?.playWhenReady = true


    }

    override fun inActive() {


        //暂停视频的播放并让封面图和 开始播放按钮 显示出来
        val pageListPlay: PageListPlay? = PageListManager.get(category)
        if (pageListPlay?.mSimpleExoPlayer == null || pageListPlay.contolView == null) return
        pageListPlay.mSimpleExoPlayer?.playWhenReady = false
        pageListPlay.contolView.removeVisibilityListener(this)
        pageListPlay.mSimpleExoPlayer?.removeListener(this)
        mCoverImg.visibility = View.VISIBLE
        mPlayBtn.visibility = View.VISIBLE
        mPlayBtn.setImageResource(R.drawable.icon_video_play)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        //点击该区域时 我们诸主动让视频控制器显示出来
        val pageListPlay: PageListPlay? = PageListManager.get(category)
        pageListPlay?.contolView?.show()
        return true
    }

    override fun onVisibilityChange(visibility: Int) {
        mPlayBtn.visibility = visibility
        mPlayBtn.setImageResource(if (isPlaying()) R.drawable.icon_video_pause else R.drawable.icon_video_play)
    }

    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {


        //监听视频播放的状态
        val pageListPlay: PageListPlay? = PageListManager.get(category)
        val exoPlayer: SimpleExoPlayer? = pageListPlay?.mSimpleExoPlayer
        if (playbackState == Player.STATE_READY && exoPlayer?.bufferedPosition != 0L && playWhenReady) {
            mCoverImg.visibility = View.GONE
            mBufferView.visibility = View.GONE
        } else if (playbackState == Player.STATE_BUFFERING) {
            mBufferView.visibility = View.VISIBLE
        }
        playing =
            playbackState == Player.STATE_READY && exoPlayer?.bufferedPosition != 0L && playWhenReady
        mPlayBtn.setImageResource(if (playing) R.drawable.icon_video_pause else R.drawable.icon_video_play)
    }
}