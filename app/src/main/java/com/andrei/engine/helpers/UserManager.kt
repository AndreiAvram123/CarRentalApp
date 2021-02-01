package com.andrei.engine.helpers

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.andrei.carrental.R
import com.andrei.engine.DTOEntities.BasicUserLoginData
import com.andrei.utils.edit
import com.andrei.utils.getIntOrNull
import com.andrei.utils.getStringOrNull
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class UserManager @Inject constructor(
        private val sharedPreferences: SharedPreferences,
        @ApplicationContext private val  context:Context
) {

    private val mUserLoginData : MutableLiveData<BasicUserLoginData> by lazy {
        MutableLiveData<BasicUserLoginData>().also {
            getUserDetailsFromStorage()
        }
    }

    val userLoginData:LiveData<BasicUserLoginData>
    get() = mUserLoginData



   fun setNewUser(basicUserLoginData: BasicUserLoginData){
       saveUserInSharedPrefs(basicUserLoginData)
       mUserLoginData.postValue(basicUserLoginData)
   }

    private fun saveUserInSharedPrefs(userLoginData:BasicUserLoginData){
        sharedPreferences.edit {
            putInt(context.getString(R.string.key_user_id),userLoginData.id)
            putString(context.getString(R.string.key_user_email),userLoginData.email)
        }
    }


    private fun getUserDetailsFromStorage() {
        GlobalScope.launch(Dispatchers.IO) {
            val email = sharedPreferences.getStringOrNull(context.getString(R.string.key_user_email))
            val id = sharedPreferences.getIntOrNull(context.getString(R.string.key_user_id))
            if (email != null && id != null) {
                mUserLoginData.postValue(BasicUserLoginData(
                        email = email,
                        id = id
                ))
            }
        }
    }

}