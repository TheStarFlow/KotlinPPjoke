package com.zzs.ppjoke_kotlin_jetpack.common.utils


import com.google.gson.Gson
import com.zzs.ppjoke_kotlin_jetpack.model.BottomBar
import com.zzs.ppjoke_kotlin_jetpack.model.Destination
import com.zzs.ppjoke_kotlin_jetpack.model.SofaTab
import com.zzs.ppjoke_kotlin_jetpack.model.Tab
import org.json.JSONObject
import java.io.InputStreamReader
import java.util.*
import kotlin.Comparator

class AppConfig {
    companion object{

        val mDestConfig = mutableMapOf<String,Destination>().apply {
            if (isEmpty()){
                val json = parseFile("destination.json")
                val jsonObj = JSONObject(json)
                val keys = jsonObj.keys()
                for (ele in keys){
                    val value = Gson().fromJson<Destination>(jsonObj[ele].toString(),Destination::class.java)
                    put(ele,value)
                }
            }
        }

        private fun parseFile(fileName:String):String{
            val manager = com.zzs.common.globals.AppGlobals.application.resources.assets
            val reader = InputStreamReader(manager.open(fileName))
            return reader.readText()
        }

        private var sBottom : BottomBar? = null

        fun getBottomBar():BottomBar?{
            if (sBottom == null){
                val barJson = parseFile("main_tabs_config.json")
                sBottom = Gson().fromJson(barJson,BottomBar::class.java)
            }
            return sBottom
        }

        private var mSofaTabs:SofaTab? = null


        fun getSofaTabs():SofaTab{
            if (mSofaTabs==null){
                val json = parseFile("sofa_tabs_config.json")
                mSofaTabs = Gson().fromJson(json,SofaTab::class.java)
                Collections.sort(mSofaTabs!!.tabs.toMutableList(),object :Comparator<Tab>{
                    override fun compare(o1: Tab?, o2: Tab?): Int {
                       if (o1==null||o2==null)return -1
                        return if (o1.index<o2.index)-1 else 1
                    }

                })
            }
           return mSofaTabs!!
        }
    }




}