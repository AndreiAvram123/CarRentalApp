package com.andrei.engine.helpers

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.andrei.carrental.entities.User
import javax.inject.Inject

class UserManager @Inject constructor(
        private val sharedPreferences: SharedPreferences
) {

    private val mUser : MutableLiveData<User> by lazy {
        MutableLiveData<User>().also {
            getUserDetailsFromStorage()
        }
    }

    val user:LiveData<User>
    get() = mUser


   fun setNewUser(userAccount: User){
       mUser.postValue(userAccount)
   }


    private fun getUserDetailsFromStorage(){

    }

}