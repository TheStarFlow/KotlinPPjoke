package com.zzs.ppjoke_kotlin_jetpack.ui.home

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.paging.ItemKeyedDataSource
import androidx.paging.PagedList
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.zzs.ppjoke_kotlin_jetpack.common.ext.logWithE
import com.zzs.ppjoke_kotlin_jetpack.data.datasource.MutableDataSource
import com.zzs.ppjoke_kotlin_jetpack.exoplayer.PageListManager
import com.zzs.ppjoke_kotlin_jetpack.exoplayer.PageListPlayDetector
import com.zzs.ppjoke_kotlin_jetpack.model.respmodel.Feed
import com.zzs.ppjoke_kotlin_jetpack.ui.AbsListFragment
import com.zzs.ppjoke_kotlin_jetpack.ui.adapter.FeedAdapter
import kotlinx.android.synthetic.main.layout_refresh_view.*

class HomeFragment : AbsListFragment<Feed, HomeViewModel>() {

    private lateinit var playDetector: PageListPlayDetector


    companion object {
        @JvmStatic
        fun newInstance(feedType: String) = HomeFragment().apply {
            val bundle = Bundle()
            bundle.putString("feedType", feedType)
            arguments = bundle
        }
    }

    override fun afterViewCreatedViewModelCreated() {
        mViewModel.feedType =
            if (arguments == null) "all" else requireArguments().getString("feedType") ?: "all"

    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        mViewModel.getFeedDataSource()?.invalidate()
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        val lastFeed = mAdapter.currentList?.get(mAdapter.itemCount - 1)
        mViewModel.loadMore(object : ItemKeyedDataSource.LoadCallback<Feed>() {
            override fun onResult(data: MutableList<Feed>) {
                if (data.isNullOrEmpty()) return
                val dataSource = MutableDataSource<Int, Feed>()
                dataSource.dataList.addAll(data)
                val pageList = dataSource.getPageList(mAdapter.currentList?.config!!)
                submitList(pageList)
            }

        }, lastFeed?.id ?: 0)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewModel.cacheFeedLiveData.observe(viewLifecycleOwner, Observer {
            submitList(it)
        })
        playDetector = PageListPlayDetector(this, mRecycler)

    }

    override fun getPageAdapter(): PagedListAdapter<Feed, RecyclerView.ViewHolder> {
        val category =
            if (arguments == null) "all" else requireArguments().getString("feedType") ?: "all"
        category.logWithE("创建TAG")
        return object : FeedAdapter(category) {

            override fun onViewAttachedToWindow(holder: FeedViewHolder) {
                super.onViewAttachedToWindow(holder)
                if (holder.isVideoItem()) {
                    playDetector.addTarget(holder.lisPlayView!!)
                }
            }

            override fun onViewDetachedFromWindow(holder: FeedViewHolder) {
                super.onViewDetachedFromWindow(holder)
                if (holder.lisPlayView != null) {
                    playDetector.removeTarget(holder.lisPlayView)
                }
            }

            override fun onCurrentListChanged(
                previousList: PagedList<Feed>?,
                currentList: PagedList<Feed>?
            ) {
                super.onCurrentListChanged(previousList, currentList)
                //这个方法是在我们每提交一次 pagelist对象到adapter 就会触发一次
                //每调用一次 adpater.submitlist
                if (previousList != null && currentList != null) {
                    if (!currentList.containsAll(previousList)) {
                        mRecycler.scrollToPosition(0)
                    }
                }
            }
        } as PagedListAdapter<Feed, RecyclerView.ViewHolder>
    }

    override fun onPause() {

        if (shouldPause) {
            playDetector.onPause()
        }
        Log.e("homefragment", "onPause: feedtype:all")
        super.onPause()
    }

    override fun onDestroy() {
        PageListManager.release("all")
        super.onDestroy()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (hidden) {
            playDetector.onPause()
        } else
            playDetector.onResume()
    }

    override fun onResume() {
        parentFragment?.apply {
            if (this.isVisible && this@HomeFragment.isVisible) {
                playDetector.onResume()
            }
        } ?: if (isVisible) {
            playDetector.onResume()
        }
        super.onResume()
    }

}