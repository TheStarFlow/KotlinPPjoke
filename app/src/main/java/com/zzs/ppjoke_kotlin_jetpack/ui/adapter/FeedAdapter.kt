package com.zzs.ppjoke_kotlin_jetpack.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.zzs.ppjoke_kotlin_jetpack.R
import com.zzs.ppjoke_kotlin_jetpack.databinding.LayoutFeedTypeImageBinding
import com.zzs.ppjoke_kotlin_jetpack.databinding.LayoutFeedTypeVideoBinding
import com.zzs.ppjoke_kotlin_jetpack.common.ext.bindData
import com.zzs.ppjoke_kotlin_jetpack.model.respmodel.Feed
import com.zzs.ppjoke_kotlin_jetpack.view.ListPlayView

open class FeedAdapter(val category:String) : PagedListAdapter<Feed, FeedAdapter.FeedViewHolder>(Feed.feedDiff) {

    lateinit var inflater: LayoutInflater

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {
        if (!this::inflater.isInitialized){
            inflater = LayoutInflater.from(parent.context)
        }//这里引入布局需要用DataBindingUtil方引入，否则会出现布局无法按照xml layout 进行布局
       return if (viewType==Feed.TYPE_IMAGE){
           val binding = DataBindingUtil.inflate<LayoutFeedTypeImageBinding>(inflater, R.layout.layout_feed_type_image,parent,false)
           FeedViewHolder(binding)
        }else{
           val binding = DataBindingUtil.inflate<LayoutFeedTypeVideoBinding>(inflater,R.layout.layout_feed_type_video,parent,false)
           FeedViewHolder(binding)
       }
    }

    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {
            holder.bindData(getItem(position))
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position)?.itemType?:0
    }

    inner class FeedViewHolder(private val binding:ViewDataBinding) :RecyclerView.ViewHolder(binding.root){

        var lisPlayView:ListPlayView? = null

        fun bindData(item: Feed?) {
            if (binding is LayoutFeedTypeImageBinding){
                binding.mFeed = item
                item?.apply {
                    binding.mFeedImg.bindData(cover,width,height,16)
                }
                binding.owner = binding.root.context as LifecycleOwner
            }else if ( binding is LayoutFeedTypeVideoBinding){
                item?.apply {
                    binding.mFeed = this
                    binding.mListPlayer.bindData(category,width,height,cover,url)
                }
                binding.owner = binding.root.context as LifecycleOwner
                lisPlayView = binding.mListPlayer

            }
        }

        fun isVideoItem() = binding is LayoutFeedTypeVideoBinding

    }

}