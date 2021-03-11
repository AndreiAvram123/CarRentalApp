package com.andrei.engine.repository.implementation

import androidx.lifecycle.*
import com.andrei.engine.CallRunner
import com.andrei.engine.State
import com.andrei.engine.helpers.UserManager
import com.andrei.engine.repository.interfaces.LoginRepository
import com.andrei.engine.repositoryInterfaces.LoginAPI
import com.andrei.engine.requestModels.LoginRequest
import com.andrei.engine.states.LoginFlowState
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
        private val userManager: UserManager,
        private val loginAPI: LoginAPI,
        private val callRunner: CallRunner
): LoginRepository{


    override val loginFlowState: MutableStateFlow<LoginFlowState>  = MutableStateFlow(
       if(userManager.isUserLoggedIn){
           LoginFlowState.LoggedIn
       }else{
           LoginFlowState.NotLoggedIn
       }
    )

    override suspend fun startLoginFlow(email: String, password: String) {

        val loginRequest = LoginRequest(
                email = email,
                password = password
        )
       callRunner.makeApiCall{loginAPI.attemptLogin(loginRequest)}.collect{
           when(it){
               is State.Success -> {
                   userManager.saveNewUser(it.data)
                   loginFlowState.emit(LoginFlowState.LoggedIn)
               }
               is State.Loading -> {
                   loginFlowState.emit(LoginFlowState.Loading)
               }
               is State.Error->{
                   when(it.error){
                        LoginFlowState.LoginError.errorInvalidEmail -> loginFlowState.emit(LoginFlowState.LoginError.IncorrectEmail)
                        LoginFlowState.LoginError.errorInvalidPassword -> loginFlowState.emit(LoginFlowState.LoginError.IncorrectPassword)
                        else -> loginFlowState.emit(LoginFlowState.LoginError.ConnectionError)
                   }
               }
           }
       }

    }

    override fun signOut() {
        userManager.signOut()
    }

}