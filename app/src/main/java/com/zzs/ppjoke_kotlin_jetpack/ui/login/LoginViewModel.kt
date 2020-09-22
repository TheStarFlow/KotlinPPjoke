package com.zzs.ppjoke_kotlin_jetpack.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagedList
import com.zzs.ppjoke_kotlin_jetpack.BasePageListViewModel
import com.zzs.ppjoke_kotlin_jetpack.BaseViewModel
import com.zzs.ppjoke_kotlin_jetpack.data.RepositoryImpl
import kotlinx.coroutines.launch

class LoginViewModel : BaseViewModel() {

    private val repository by lazy {
        RepositoryImpl.get()
    }

    fun saveUser(nickName: String, figure2: String, expires_time: Long, openId: String) {
        viewModelScope.launch {
            try {
                val user = repository.userInsert(nickName, figure2, expires_time, openId)
                UserManager.get().save(user)
            } catch (e: Exception) {
                onError.value = e.toString()
            }
        }
    }

}