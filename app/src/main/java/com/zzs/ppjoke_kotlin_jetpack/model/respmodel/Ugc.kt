package com.zzs.ppjoke_kotlin_jetpack.model.respmodel

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import com.zzs.ppjoke_kotlin_jetpack.model.ObserverDelegate
import java.io.Serializable
import kotlin.properties.Delegates

data class Ugc(
    val commentCount: Int =0,
    var hasDissed: Boolean = false,
    val hasFavorite: Boolean = false,
    var likeCount:Int = 0,
    val shareCount: Int = 0,
    var hasLiked:Boolean,
):BaseObservable(),Serializable{

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Ugc

        if (commentCount != other.commentCount) return false
        if (hasDissed != other.hasDissed) return false
        if (hasFavorite != other.hasFavorite) return false
        if (likeCount != other.likeCount) return false
        if (shareCount != other.shareCount) return false
        if (hasLiked != other.hasLiked) return false

        return true
    }

    override fun hashCode(): Int {
        var result = commentCount
        result = 31 * result + hasDissed.hashCode()
        result = 31 * result + hasFavorite.hashCode()
        result = 31 * result + likeCount
        result = 31 * result + shareCount
        result = 31 * result + hasLiked.hashCode()
        return result
    }
}