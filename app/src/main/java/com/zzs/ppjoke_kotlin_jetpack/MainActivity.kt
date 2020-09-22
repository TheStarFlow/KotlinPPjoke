package com.zzs.ppjoke_kotlin_jetpack

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.zzs.ppjoke_kotlin_jetpack.common.utils.AppConfig
import com.zzs.ppjoke_kotlin_jetpack.common.utils.NavGraphBuilder
import com.zzs.ppjoke_kotlin_jetpack.common.utils.StatusBar
import com.zzs.ppjoke_kotlin_jetpack.ui.login.UserManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        StatusBar.fisSystemBar(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        container.fitsSystemWindows = true
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        navView.setupWithNavController(navController)
        NavGraphBuilder.build(navController, this, R.id.nav_host_fragment)

        navView.setOnNavigationItemSelectedListener { item ->

            val dst = AppConfig.mDestConfig
            dst.forEach {
                val dest = it.value
                if (!UserManager.get().isLogin() && dest.needLogin && dest.id == item.itemId) {
                    UserManager.get().login(this).observe(this, Observer {
                        navView.selectedItemId = item.itemId
                    })
                    return@setOnNavigationItemSelectedListener false
                }
            }
            navController.navigate(item.itemId)
            item.title.isNotEmpty()
        }
    }
}