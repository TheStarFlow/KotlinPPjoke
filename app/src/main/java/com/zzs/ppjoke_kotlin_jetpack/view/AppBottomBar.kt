package com.zzs.ppjoke_kotlin_jetpack.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.AttributeSet
import android.util.TypedValue
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomnavigation.LabelVisibilityMode
import com.zzs.ppjoke_kotlin_jetpack.R
import com.zzs.ppjoke_kotlin_jetpack.common.utils.AppConfig

class AppBottomBar : BottomNavigationView {
    constructor(context: Context) : super(context)
    @SuppressLint("RestrictedApi")
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs){
        val bar = AppConfig.getBottomBar()
        val tabs = bar?.tabs
        val states = Array(2){IntArray(2)}
        states[0] = intArrayOf(android.R.attr.state_selected)
        states[1] = IntArray(0)
        val colors = intArrayOf(Color.parseColor(bar?.activeColor),
            Color.parseColor(bar?.inActiveColor))
        val colorState = ColorStateList(states,colors)
        itemIconTintList = colorState;
        itemTextColor = colorState
        labelVisibilityMode = LabelVisibilityMode.LABEL_VISIBILITY_LABELED
        selectedItemId = bar!!.selectTab
        tabs?.filter {
            it.enable
        }?.forEach {
            val id = getItemId(it.pageUrl)
            val item = menu.add(0,id,it.index,it.title)
            item.setIcon(icons[it.index])
        }
        tabs?.filter {
            it.enable
        }?.forEach {
            val size = px2dp(it.size)
            val menuView = getChildAt(0) as BottomNavigationMenuView
            val item = menuView.getChildAt(it.index) as BottomNavigationItemView
            item.setIconSize(size.toInt())
            if (it.title.isNullOrEmpty()){
                item.setIconTintList(ColorStateList.valueOf(Color.parseColor(it.tintColor)))
                item.setShifting(false)
            }
        }
    }
    @SuppressLint("RestrictedApi")
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    fun px2dp(value:Int) = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,value.toFloat(),resources.displayMetrics)

    fun getItemId(uri:String?):Int{
        return AppConfig.mDestConfig[uri]!!.id
    }

    companion object{
        private val icons = mutableListOf(R.drawable.icon_tab_home, R.drawable.icon_tab_sofa, R.drawable.icon_tab_publish, R.drawable.icon_tab_find, R.drawable.icon_tab_mine)
    }
}