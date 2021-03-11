package com.andrei.engine.helpers

import android.content.Context
import android.content.SharedPreferences
import com.andrei.DI.annotations.DefaultGlobalScope
import com.andrei.carrental.R
import com.andrei.engine.DTOEntities.BasicUserLoginData
import com.andrei.engine.responseModels.LoginResponse
import com.andrei.utils.*
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject constructor(
        private val sharedPreferences: SharedPreferences,
        @ApplicationContext private val  context:Context,
        private val tokenManager: TokenManager,
        @DefaultGlobalScope  private val coroutineScope: CoroutineScope

) {

    private val _authenticationState:MutableStateFlow<AuthenticationState> = MutableStateFlow(if(isUserLoggedIn){
        AuthenticationState.AUTHENTICATED
    }else{
        AuthenticationState.NOT_AUTHENTICATED
    })

    val authenticationState:StateFlow<AuthenticationState>
    get() = _authenticationState.asStateFlow()


    val isUserLoggedIn:Boolean
    get() = areUserDetailsValid() && tokenManager.isCurrentTokenValid()


    init {
        coroutineScope.launch {
            tokenManager.tokenState.collect {
                if(it is TokenState.Invalid){
                    _authenticationState.emit(AuthenticationState.NOT_AUTHENTICATED)
                }
            }
        }
    }


    fun signOut(){
        removeUserFromPersistence()
        tokenManager.clearToken()
    }

    private fun areUserDetailsValid():Boolean{
        val email = sharedPreferences.getStringOrNull(context.getString(R.string.key_user_email))
        val id = sharedPreferences.getLongOrZero(context.getString(R.string.key_user_id))
        return email != null && id != 0L
    }



   fun saveNewUser(loginResponse: LoginResponse){
       saveUserInStorage(loginResponse.basicUserLoginData)
       tokenManager.setNewToken(loginResponse.token)
       coroutineScope.launch { _authenticationState.emit(AuthenticationState.AUTHENTICATED)}
   }

    private fun saveUserInStorage(userLoginData:BasicUserLoginData){
        sharedPreferences.edit {
            putLong(context.getString(R.string.key_user_id),userLoginData.id)
            putString(context.getString(R.string.key_user_email),userLoginData.email)
        }
    }

    private fun removeUserFromPersistence(){
        sharedPreferences.removeValue(context.getString(R.string.key_user_id))
        sharedPreferences.removeValue(context.getString(R.string.key_user_email))
    }

    enum class AuthenticationState{
        AUTHENTICATED,NOT_AUTHENTICATED
    }

}