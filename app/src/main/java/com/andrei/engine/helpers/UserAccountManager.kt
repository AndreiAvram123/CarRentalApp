package com.andrei.engine.helpers

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.andrei.engine.DTOEntities.UserAccount
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class UserAccountManager @Inject constructor(
        private val sharedPreferences: SharedPreferences
) {

    val userAccountDetails : MutableLiveData<UserAccount> by lazy {
        MutableLiveData<UserAccount>().also {
            getUserDetailsFromStorage()
        }
    }



    private fun getUserDetailsFromStorage(){

    }

}