package com.zzs.ppjoke_kotlin_jetpack.common.ext

import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.zzs.common.utils.PixUtils


@BindingAdapter("bindUrl","bindIsCircle")
fun bindImage(
    view: ImageView,
    url: String?,
    isCircle: Boolean
){
    if (TextUtils.isEmpty(url))return
    Glide.with(view).load(url).apply {
        if (isCircle){
            transform(CircleCrop())
        }
        val params = view.layoutParams
        if (params!=null&&params.width>0&&params.height>0){
            override(params.width, params.height)
        }
        into(view)
    }
}

fun ImageView.bindData(url: String?, width: Int, height: Int, marginLeft: Int, maxWidth: Int = PixUtils.getScreenWidth(), maxHeight: Int = PixUtils.getScreenWidth()){
    if (width<=0||height<=0){
        Glide.with(this).load(url).into(object : SimpleTarget<Drawable>() {
            override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                val widthInner = resource.intrinsicWidth
                val heightInner = resource.intrinsicHeight
                setSize(widthInner,heightInner,marginLeft,maxWidth,maxHeight,this@bindData)
                setImageDrawable(resource)
            }
        })
        return
    }
    setSize(width,height,marginLeft,maxWidth,maxHeight,this)
    bindImage(this,url,false)
}


private fun setSize(width: Int, height: Int, marginLeft: Int, maxWidth: Int, maxHeight: Int,view:ImageView) {
    val finalWidth: Int
    val finalHeight: Int
    if (width > height) {
        finalWidth = maxWidth
        finalHeight = (height / (width * 1.0f / finalWidth)).toInt()
    } else {
        finalHeight = maxHeight
        finalWidth = (width / (height * 1.0f / finalHeight)).toInt()
    }
    val params: ViewGroup.LayoutParams = view.layoutParams
    params.width = finalWidth
    params.height = finalHeight
    if (params is FrameLayout.LayoutParams) {
        params.leftMargin = if (height > width) PixUtils.dp2px(marginLeft) else 0
    } else if (params is LinearLayout.LayoutParams) {
        params.leftMargin = if (height > width) PixUtils.dp2px(marginLeft) else 0
    }
    view.layoutParams = params
}

fun ImageView.setBlurImage(url:String,radius:Float){
    Glide.with(this).load(url).override(50)
        .into(object :SimpleTarget<Drawable>(){
            override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                    background = resource
            }
        })
}


fun ImageView.loadImage(url:String){
    Glide.with(this).load(url).into(this)
}

fun View.onClick(block:()->Unit){
    this.setOnClickListener { block() }
}