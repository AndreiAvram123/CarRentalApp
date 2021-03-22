package com.andrei.engine.helpers

import android.content.Context
import android.content.SharedPreferences
import com.andrei.DI.annotations.DefaultGlobalScope
import com.andrei.carrental.R
import com.andrei.carrental.UserDataManager
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
        private val tokenManager: TokenManager,
        @DefaultGlobalScope  private val coroutineScope: CoroutineScope,
        private val userDataManager: UserDataManager
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
        val email = userDataManager.email
        val id = userDataManager.userID
        return email != null && id != 0L
    }



   fun saveNewUser(loginResponse: LoginResponse){
       saveUserInStorage(loginResponse.basicUserLoginData)
       tokenManager.setNewToken(loginResponse.token)
       coroutineScope.launch { _authenticationState.emit(AuthenticationState.AUTHENTICATED)}
   }

    private fun saveUserInStorage(userLoginData:BasicUserLoginData){
        userDataManager.apply {
            email = userLoginData.email
            userID = userLoginData.id
        }
    }

    private fun removeUserFromPersistence(){
        userDataManager.apply {
            email = null
            userID = 0
        }
    }

    enum class AuthenticationState{
        AUTHENTICATED,NOT_AUTHENTICATED
    }

}