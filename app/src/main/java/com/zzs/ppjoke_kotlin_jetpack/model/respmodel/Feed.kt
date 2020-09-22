package com.zzs.ppjoke_kotlin_jetpack.model.respmodel

import androidx.databinding.Bindable
import androidx.recyclerview.widget.DiffUtil
import java.io.Serializable
import kotlin.properties.Delegates

data class Feed(
    val activityIcon: String,
    val activityText: String,
    val authorId: Long,
    val cover: String,
    val createTime: Long,
    val duration: Double,
    val feeds_text: String,
    val height: Int,
    val id: Long,
    val itemId: Long,
    val itemType: Int,
    val url: String,
    val width: Int,
    val author:User,
    val topComment:Comment,
    val ugc: Ugc
) :Serializable{

    companion object {
        val feedDiff = object :DiffUtil.ItemCallback<Feed>(){
            override fun areItemsTheSame(oldItem: Feed, newItem: Feed): Boolean {
                return oldItem.id==newItem.id
            }

            override fun areContentsTheSame(oldItem: Feed, newItem: Feed): Boolean {
                return oldItem == newItem
            }
        }

        const val TYPE_VIDEO = 2
        const val TYPE_IMAGE = 1
        const val CACHE_KEY = "database_cache_key_feed"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Feed

        if (activityIcon != other.activityIcon) return false
        if (activityText != other.activityText) return false
        if (authorId != other.authorId) return false
        if (cover != other.cover) return false
        if (createTime != other.createTime) return false
        if (duration != other.duration) return false
        if (feeds_text != other.feeds_text) return false
        if (height != other.height) return false
        if (id != other.id) return false
        if (itemId != other.itemId) return false
        if (itemType != other.itemType) return false
        if (url != other.url) return false
        if (width != other.width) return false
        if (author != other.author) return false
        if (topComment != other.topComment) return false
        if (ugc != other.ugc) return false

        return true
    }

    override fun hashCode(): Int {
        var result = activityIcon.hashCode()
        result = 31 * result + activityText.hashCode()
        result = 31 * result + authorId.hashCode()
        result = 31 * result + cover.hashCode()
        result = 31 * result + createTime.hashCode()
        result = 31 * result + duration.hashCode()
        result = 31 * result + feeds_text.hashCode()
        result = 31 * result + height
        result = 31 * result + id.hashCode()
        result = 31 * result + itemId.hashCode()
        result = 31 * result + itemType
        result = 31 * result + url.hashCode()
        result = 31 * result + width
        result = 31 * result + author.hashCode()
        result = 31 * result + topComment.hashCode()
        result = 31 * result + ugc.hashCode()
        return result
    }


}