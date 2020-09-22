package com.zzs.ppjoke_kotlin_jetpack.common.utils

import android.content.ComponentName
import androidx.fragment.app.FragmentActivity
import androidx.navigation.ActivityNavigator
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.NavGraphNavigator
import com.zzs.ppjoke_kotlin_jetpack.common.navigator.FixFragmentNavigator

class NavGraphBuilder {
    companion object{
         fun build(navController: NavController,activity: FragmentActivity,id:Int){
             val provider = navController.navigatorProvider;
             val navGraph = NavGraph(NavGraphNavigator(provider))
             //val fragmentNavigator  = provider.getNavigator(FragmentNavigator::class.java)
             val fragmentNavigator = FixFragmentNavigator(activity,activity.supportFragmentManager,id)
             provider.addNavigator(fragmentNavigator)
             val activityNavigator = provider.getNavigator(ActivityNavigator::class.java)
             val destConfig = AppConfig.mDestConfig
             for (dst in destConfig.values){
                 if (dst.isFragment){
                     val dest = fragmentNavigator.createDestination()
                     navGraph.addDestination(dest.apply {
                         className = dst.className
                         setId(dst.id)
                         addDeepLink(dst.pageUrl)
                     })
                 }else{
                     val dest = activityNavigator.createDestination()
                     navGraph.addDestination(dest.apply {
                         setId(dst.id)
                         addDeepLink(dst.pageUrl)
                         setComponentName(ComponentName(com.zzs.common.globals.AppGlobals.application.packageName,dst.className))
                     })
                 }
                 if (dst.asStarter){
                     navGraph.startDestination = dst.id
                 }

             }
             navController.graph =navGraph

         }
    }
}