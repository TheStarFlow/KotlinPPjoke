package com.zzs.ppjoke_kotlin_jetpack.model.respmodel

import java.io.Serializable

data class User(
    val avatar: String,
    val commentCount: Int,
    val description: String,
    val expires_time: Long,
    val favoriteCount: Int,
    val feedCount: Int,
    val followCount: Int,
    val followerCount: Int,
    val hasFollow: Boolean,
    val historyCount: Int,
    val id: Long,
    val likeCount: Int,
    val name: String,
    val qqOpenId: String,
    val score: Int,
    val topCommentCount: Int,
    val userId: Long
) :Serializable{
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as User

        if (avatar != other.avatar) return false
        if (commentCount != other.commentCount) return false
        if (description != other.description) return false
        if (expires_time != other.expires_time) return false
        if (favoriteCount != other.favoriteCount) return false
        if (feedCount != other.feedCount) return false
        if (followCount != other.followCount) return false
        if (followerCount != other.followerCount) return false
        if (hasFollow != other.hasFollow) return false
        if (historyCount != other.historyCount) return false
        if (id != other.id) return false
        if (likeCount != other.likeCount) return false
        if (name != other.name) return false
        if (qqOpenId != other.qqOpenId) return false
        if (score != other.score) return false
        if (topCommentCount != other.topCommentCount) return false
        if (userId != other.userId) return false

        return true
    }

    override fun hashCode(): Int {
        var result = avatar.hashCode()
        result = 31 * result + commentCount
        result = 31 * result + description.hashCode()
        result = 31 * result + expires_time.hashCode()
        result = 31 * result + favoriteCount
        result = 31 * result + feedCount
        result = 31 * result + followCount
        result = 31 * result + followerCount
        result = 31 * result + hasFollow.hashCode()
        result = 31 * result + historyCount
        result = 31 * result + id.hashCode()
        result = 31 * result + likeCount
        result = 31 * result + name.hashCode()
        result = 31 * result + qqOpenId.hashCode()
        result = 31 * result + score
        result = 31 * result + topCommentCount
        result = 31 * result + userId.hashCode()
        return result
    }
}