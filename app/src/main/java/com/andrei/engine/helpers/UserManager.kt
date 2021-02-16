package com.andrei.engine.helpers

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.andrei.carrental.R
import com.andrei.engine.DTOEntities.BasicUserLoginData
import com.andrei.engine.responseModels.LoginResponse
import com.andrei.utils.edit
import com.andrei.utils.getIntOrNull
import com.andrei.utils.getStringOrNull
import com.andrei.utils.removeValue
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class UserManager @Inject constructor(
        private val sharedPreferences: SharedPreferences,
        @ApplicationContext private val  context:Context,
        private val tokenManager: TokenManager

) {

    private val _userLoginData : MutableLiveData<BasicUserLoginData> by lazy {
        MutableLiveData<BasicUserLoginData>().also {
            getUserDetailsFromStorage()
        }
    }


    val isUserLoggedIn :MediatorLiveData<Boolean> by lazy {
        MediatorLiveData<Boolean>().apply {
            addSource(_userLoginData){
                if(it == null){
                    value = false
                }else{
                    if(tokenManager.userToken.value is TokenState.Valid){
                        value = true
                    }
                }
            }
            addSource(tokenManager.userToken){
                if(it is TokenState.Invalid){
                    value = false
                }else{
                    if(it is TokenState.Valid){
                        value = true
                    }
                }
            }
        }
    }


    val userLoginData:LiveData<BasicUserLoginData>
    get() = _userLoginData



   fun saveNewUser(loginResponse: LoginResponse){
       saveUserInSharedPrefs(loginResponse.basicUserLoginData)
       _userLoginData.postValue(loginResponse.basicUserLoginData)
       tokenManager.setNewToken(loginResponse.token)

   }

    private fun saveUserInSharedPrefs(userLoginData:BasicUserLoginData){
        sharedPreferences.edit {
            putInt(context.getString(R.string.key_user_id),userLoginData.id)
            putString(context.getString(R.string.key_user_email),userLoginData.email)
        }
    }

    fun signOut(){
       removeUserFromPersistence()
        tokenManager.clearToken()
        _userLoginData.postValue(null)
    }

    private fun removeUserFromPersistence(){
        sharedPreferences.removeValue(context.getString(R.string.key_user_id))
        sharedPreferences.removeValue(context.getString(R.string.key_user_email))
    }


    private fun getUserDetailsFromStorage() {
        GlobalScope.launch(Dispatchers.IO) {
            val email = sharedPreferences.getStringOrNull(context.getString(R.string.key_user_email))
            val id = sharedPreferences.getIntOrNull(context.getString(R.string.key_user_id))
            if (email != null && id != null) {
                _userLoginData.postValue(BasicUserLoginData(
                        email = email,
                        id = id
                ))
            }else{
                _userLoginData.postValue(null)
            }
        }
    }

}