package com.zzs.ppjoke_kotlin_jetpack.exoplayer

import android.util.Pair
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView

class PageListPlayDetector {

    val  mTargets = mutableListOf<IPlayTarget>()

    fun addTarget(target: IPlayTarget) = mTargets.add(target)

    fun removeTarget(target: IPlayTarget?) = mTargets.remove(target)

    private val mRecyclerView:RecyclerView

    var playingTarget:IPlayTarget?=null

    constructor(owner:LifecycleOwner,recyclerView: RecyclerView){
        mRecyclerView = recyclerView
        owner.lifecycle.addObserver(object :LifecycleEventObserver{
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                if (event == Lifecycle.Event.ON_DESTROY) {
                    playingTarget = null
                    mTargets.clear()
                    mRecyclerView.removeCallbacks(autoRun)
                    recyclerView.removeOnScrollListener(scrollListener)
                    owner.lifecycle.removeObserver(this)
                }
            }
        })
        mRecyclerView.adapter?.registerAdapterDataObserver(dataObserver)
        mRecyclerView.addOnScrollListener(scrollListener)
    }

    private val dataObserver = object :RecyclerView.AdapterDataObserver(){
        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                autoPlay()
        }
    }

    val scrollListener: RecyclerView.OnScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                autoPlay()
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if (dx == 0 && dy == 0) {
                //时序问题。当执行了AdapterDataObserver#onItemRangeInserted  可能还没有被布局到RecyclerView上。
                //所以此时 recyclerView.getChildCount()还是等于0的。
                //等childView 被布局到RecyclerView上之后，会执行onScrolled（）方法
                //并且此时 dx,dy都等于0
                postAutoPlay()
            } else {
                //如果有正在播放的,且滑动时被划出了屏幕 则 停止他
                playingTarget?.apply {
                    if (this.isPlaying()&&!isTargetInBounds(this)){
                        inActive()
                    }
                }
            }
        }
    }

    val autoRun = Runnable { autoPlay() }
    fun postAutoPlay() = mRecyclerView.post(autoRun)

    private fun autoPlay() {
        if (mTargets.isEmpty() || mRecyclerView.childCount <= 0) {
            return
        }
        if (playingTarget != null && playingTarget!!.isPlaying() && isTargetInBounds(playingTarget!!)) {
            return
        }
        var activeTarget: IPlayTarget? = null
        for (target in mTargets) {
            val inBounds: Boolean = isTargetInBounds(target)
            if (inBounds) {
                activeTarget = target
                break
            }
        }
        if (activeTarget != null) {
            if (playingTarget != null) {
                playingTarget!!.inActive()
            }
            playingTarget = activeTarget
            activeTarget.onActive()
        }
    }

    private fun isTargetInBounds(target: IPlayTarget): Boolean {
        val owner: ViewGroup = target.owner()
        ensureRecyclerViewLocation()
        if (!owner.isShown || !owner.isAttachedToWindow) {
            return false
        }
        val location = IntArray(2)
        owner.getLocationOnScreen(location)
        val center = location[1] + owner.height / 2

        //承载视频播放画面的ViewGroup它需要至少一半的大小 在RecyclerView上下范围内
        return center >= rvLocation!!.first!! && center <= rvLocation!!.second!!
    }

    private var rvLocation: Pair<Int, Int>? = null

    private fun ensureRecyclerViewLocation(): Pair<Int, Int> {
        if (rvLocation == null) {
            val location = IntArray(2)
            mRecyclerView.getLocationOnScreen(location)
            val top = location[1]
            val bottom = top + mRecyclerView.height
            rvLocation = Pair<Int, Int>(top, bottom)
        }
        return rvLocation!!
    }

    fun onPause() {
        if (playingTarget != null) {
            playingTarget!!.inActive()
        }
    }

    fun onResume() {
        if (playingTarget != null) {
            playingTarget!!.onActive()
        }
    }

}