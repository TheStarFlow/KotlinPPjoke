package com.zzs.ppjoke_kotlin_jetpack.model.respmodel

import java.io.Serializable

data class Comment(
    val author: User,
    val commentCount: Int,
    val commentId: Long,
    val commentText: String,
    val commentType: Int,
    val createTime: Long,
    val hasLiked: Boolean,
    val height: Int,
    val id: Long,
    val imageUrl: String,
    val itemId: Long,
    val likeCount: Int,
    val ugc: Ugc,
    val userId: Int,
    val videoUrl: String,
    val width: Int,
):Serializable{
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Comment

        if (author != other.author) return false
        if (hasLiked != other.hasLiked) return false
        if (likeCount != other.likeCount) return false
        if (ugc != other.ugc) return false

        return true
    }

    override fun hashCode(): Int {
        var result = author.hashCode()
        result = 31 * result + hasLiked.hashCode()
        result = 31 * result + likeCount
        result = 31 * result + ugc.hashCode()
        return result
    }
}

