package com.zzs.ppjoke_kotlin_jetpack.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.tencent.connect.UserInfo
import com.tencent.connect.auth.QQToken
import com.tencent.connect.common.Constants
import com.tencent.tauth.IUiListener
import com.tencent.tauth.Tencent
import com.tencent.tauth.UiError
import com.zzs.ppjoke_kotlin_jetpack.R
import com.zzs.ppjoke_kotlin_jetpack.common.ext.onClick
import com.zzs.ppjoke_kotlin_jetpack.common.ext.toast
import kotlinx.android.synthetic.main.activity_layout_login.*
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {
    lateinit var tencent: Tencent

    val mViewModel by viewModels<LoginViewModel>()

    val loginListener = object :IUiListener{
        override fun onComplete(p0: Any?) {
            if (p0 is JSONObject) {
                val openId = p0.getString("openid")
                val access_token = p0.getString("access_token")
                val expires_in = p0.getString("expires_in")
                val expires_time = p0.getLong("expires_time")
                tencent.setAccessToken(access_token,expires_in)
                tencent.openId = openId
                getToken(tencent.qqToken,expires_time,openId)

            }
        }

        override fun onError(p0: UiError?) {
            "登录失败:reason:${p0?.toString()}".toast(this@LoginActivity)
        }

        override fun onCancel() {
            "登录取消".toast(this@LoginActivity)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_layout_login)
        action_close.onClick {
            finish()
        }
        onLogin()
    }

    fun onLogin() {
        action_login.onClick {
            if (!this::tencent.isInitialized) {
                tencent = Tencent.createInstance("101902367", applicationContext)
            }
            tencent.login(this, "all", loginListener)
        }
    }

    fun getToken(qqToken: QQToken, expires_time: Long, openId: String){
        val userInfo = UserInfo(applicationContext,qqToken)
        userInfo.getUserInfo(object :IUiListener{
            override fun onComplete(p0: Any?) {
                if (p0 is JSONObject){
                    val nickName = p0.getString("nickname")
                    val figure2 = p0.getString("figureurl_2")
                    saveUser(nickName,figure2,expires_time,openId)
                }
            }

            override fun onError(p0: UiError?) {
                "登录失败:reason:${p0?.toString()}".toast(this@LoginActivity)
            }

            override fun onCancel() {
                "登录取消".toast(this@LoginActivity)
            }
        })
    }

    fun saveUser(nickName: String, figure2: String, expires_time: Long, openId: String) {
        mViewModel.saveUser(nickName,figure2,expires_time,openId)
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==Constants.REQUEST_LOGIN){
            Tencent.onActivityResultData(requestCode, resultCode, data, loginListener)
        }
    }
}