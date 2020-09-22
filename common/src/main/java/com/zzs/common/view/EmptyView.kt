package com.zzs.common.view

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.annotation.DrawableRes
import com.zzs.common.R
import kotlinx.android.synthetic.main.layout_empty_v.view.*

class EmptyView: LinearLayout {

    constructor(context: Context) : this(context,null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs,0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ){  orientation = VERTICAL
        gravity = Gravity.CENTER
        LayoutInflater.from(context).inflate(R.layout.layout_empty_v,this,true)

    }

    fun setEmptyIcon(@DrawableRes resId:Int){
        empty_icon.setImageResource(resId)
    }

    fun setButton(title:String,onClickListener: OnClickListener){
        TextUtils.isEmpty(title).also { empty ->
            if (empty){
                empty_action.visibility = GONE
            }else{
                empty_action.text = title
                empty_action.visibility = visibility
            }
            empty_action.setOnClickListener(onClickListener)
        }
    }


}