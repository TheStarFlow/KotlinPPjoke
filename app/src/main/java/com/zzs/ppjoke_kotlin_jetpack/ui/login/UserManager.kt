package com.zzs.ppjoke_kotlin_jetpack.ui.login

import android.content.Context
import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.zzs.network.database.DataBaseManager
import com.zzs.ppjoke_kotlin_jetpack.model.respmodel.User

class UserManager private constructor() {

    companion object{
        const val User_key = "cache_user"
        private val instance by lazy{
            UserManager()
        }

        fun get() = instance
    }

    private val userLiveData = MutableLiveData<User>()
    private var mUser:User? = null

    init {
        val user = DataBaseManager.getCache(User_key)
        if ( user is User)
        if (user.expires_time<System.currentTimeMillis()){
            mUser = user
        }
    }

    fun  save(user:User){
        mUser = user
        DataBaseManager.insert(User_key,user)
        if (userLiveData.hasObservers()){
            userLiveData.postValue(user)
        }
    }

    fun login(context: Context):LiveData<User>{
        return userLiveData.also {
            context.startActivity( Intent(context,LoginActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            })
        }

    }

    fun isLogin():Boolean{
        return mUser!=null && mUser!!.expires_time<System.currentTimeMillis()
    }

    fun getUserId():Long{
        return mUser?.userId?:0
    }
}