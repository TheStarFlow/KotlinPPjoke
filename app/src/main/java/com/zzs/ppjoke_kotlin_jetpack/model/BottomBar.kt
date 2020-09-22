package com.zzs.ppjoke_kotlin_jetpack.model

data class BottomBar(
    val activeColor: String,
    val inActiveColor: String,
    val selectTab:Int,
    val tabs: List<Tabs>? = null) {
    /**
     * activeColor : #333333
     * inActiveColor : #666666
     * selectTab : 0
     * tabs : [{"size":24,"enable":true,"index":0,"pageUrl":"main/tabs/home","title":"首页"},{"size":24,"enable":true,"index":1,"pageUrl":"main/tabs/sofa","title":"沙发"},{"size":40,"enable":true,"index":2,"tintColor":"#ff678f","pageUrl":"main/tabs/publish","title":""},{"size":24,"enable":true,"index":3,"pageUrl":"main/tabs/find","title":"发现"},{"size":24,"enable":true,"index":4,"pageUrl":"main/tabs/my","title":"我的"}]
     */
    class Tabs (
        val size :Int,
        val enable :Boolean,
        val index :Int,
        val pageUrl: String,
        val title: String,
        val tintColor: String,
    ){
        /**
         * size : 24
         * enable : true
         * index : 0
         * pageUrl : main/tabs/home
         * title : 首页
         * tintColor : #ff678f
         */
        
    }
}