package com.andrei.engine.helpers

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.andrei.carrental.entities.UserAccount
import com.andrei.engine.DTOEntities.LoginResponse
import javax.inject.Inject

class UserAccountManager @Inject constructor(
        private val sharedPreferences: SharedPreferences
) {

    private val mUserAccount : MutableLiveData<UserAccount> by lazy {
        MutableLiveData<UserAccount>().also {
            getUserDetailsFromStorage()
        }
    }

    val userAccount:LiveData<UserAccount>
    get() = mUserAccount


   fun setNewUserAccount(userAccount: UserAccount){
       mUserAccount.postValue(userAccount)
   }


    private fun getUserDetailsFromStorage(){

    }

}