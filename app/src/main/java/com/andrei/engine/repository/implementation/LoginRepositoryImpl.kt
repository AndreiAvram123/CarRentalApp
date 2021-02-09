package com.andrei.engine.repository.implementation

import androidx.lifecycle.*
import com.andrei.engine.CallRunner
import com.andrei.engine.State
import com.andrei.engine.helpers.UserManager
import com.andrei.engine.repository.interfaces.LoginRepository
import com.andrei.engine.repositoryInterfaces.LoginAPI
import com.andrei.engine.requestModels.LoginRequest
import com.andrei.engine.states.LoginFlowState
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
        private val userManager: UserManager,
        private val loginAPI: LoginAPI,
        private val callRunner: CallRunner
): LoginRepository{


    override val loginFlowState: MediatorLiveData<LoginFlowState> by lazy {
        MediatorLiveData<LoginFlowState>().apply {
            addSource(userManager.isUserLoggedIn){
                value = if (it){
                    LoginFlowState.LoggedIn
                }else{
                    LoginFlowState.NotLoggedIn
                }
            }
        }
    }


    override suspend fun startLoginFlow(email: String, password: String) {

        val loginRequest = LoginRequest(
                email = email,
                password = password
        )
       callRunner.makeApiCall(loginAPI.attemptLogin(loginRequest)){
           when(it){
               is State.Success -> {
                   if (it.data != null) {
                       userManager.saveNewUser(it.data)
                       loginFlowState.postValue(LoginFlowState.LoggedIn)
                   }
               }
               is State.Loading -> {
                   loginFlowState.postValue(LoginFlowState.Loading)
               }
               is State.Error->{
                   when(it.error){
                        LoginFlowState.LoginError.errorInvalidEmail -> loginFlowState.postValue(LoginFlowState.LoginError.IncorrectEmail)
                        LoginFlowState.LoginError.errorInvalidPassword -> loginFlowState.postValue(LoginFlowState.LoginError.IncorrectPassword)
                        else -> loginFlowState.postValue(LoginFlowState.LoginError.ConnectionError)
                   }
               }
           }
       }

    }

    override fun signOut() {
        userManager.signOut()
    }

}