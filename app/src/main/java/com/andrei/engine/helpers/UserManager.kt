package com.andrei.engine.helpers

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.andrei.carrental.R
import com.andrei.carrental.entities.User
import com.andrei.engine.DTOEntities.BasicUser
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

    private val mUser : MutableLiveData<BasicUser> by lazy {
        MutableLiveData<BasicUser>().also {
            getUserDetailsFromStorage()
        }
    }

    val user:LiveData<BasicUser>
    get() = mUser



   fun setNewUser(basicUser: BasicUser){
       saveUserInSharedPrefs(basicUser)
       mUser.postValue(basicUser)
   }

    private fun saveUserInSharedPrefs(user:BasicUser){
        sharedPreferences.edit {
            putInt(context.getString(R.string.key_user_id),user.id)
            putString(context.getString(R.string.key_user_email),user.email)
        }
    }


    private fun getUserDetailsFromStorage() {
        GlobalScope.launch(Dispatchers.IO) {
            val email = sharedPreferences.getStringOrNull(context.getString(R.string.key_user_email))
            val id = sharedPreferences.getIntOrNull(context.getString(R.string.key_user_id))
            if (email != null && id != null) {
                mUser.postValue(BasicUser(
                        email = email,
                        id = id
                ))
            }
        }
    }

}