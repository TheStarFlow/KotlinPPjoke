package com.zzs.ppjoke_kotlin_jetpack.model

data class Destination(
    val isFragment: Boolean,
    val asStarter: Boolean,
    val needLogin: Boolean,
    val pageUrl: String,
    val className: String,
    val id: Int

) {
}