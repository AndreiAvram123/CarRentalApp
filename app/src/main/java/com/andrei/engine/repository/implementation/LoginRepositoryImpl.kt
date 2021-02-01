package com.andrei.engine.repository.implementation

import androidx.lifecycle.*
import com.andrei.engine.CallRunner
import com.andrei.engine.DTOEntities.BasicUserLoginData
import com.andrei.engine.State
import com.andrei.engine.helpers.TokenManager
import com.andrei.engine.helpers.TokenState
import com.andrei.engine.helpers.UserManager
import com.andrei.engine.repository.interfaces.LoginRepository
import com.andrei.engine.repositoryInterfaces.AuthRepoInterface
import com.andrei.engine.requestModels.LoginRequest
import com.andrei.engine.states.LoginFlowState
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val userManager: UserManager,
    private val tokenManager: TokenManager,
    private val authRepo: AuthRepoInterface,
    private val callRunner: CallRunner
): LoginRepository{

    override val isUserLoggedIn: MediatorLiveData<Boolean>  by lazy {
        MediatorLiveData<Boolean>().apply {
            addSource(userLoginData){
                if(it!=null && tokenManager.userToken.value is TokenState.Valid ){
                    value = true
                }
            }
            addSource(tokenManager.userToken){
                if(it is TokenState.Valid && userLoginData.value != null){
                    value = true
                }
            }
        }
    }

    override val userLoginData: LiveData<BasicUserLoginData> by lazy {
        userManager.userLoginData
    }

    override val loginFlowState: MutableLiveData<LoginFlowState> by lazy {
        MutableLiveData()
    }

    override suspend fun startLoginFlow(email: String, password: String) {

        val loginRequest = LoginRequest(
                email = email,
                password = password
        )
       callRunner.makeApiCall(authRepo.attemptLogin(loginRequest)){
           when(it){
               is State.Success -> {
                   if (it.data != null) {
                       userManager.setNewUser(it.data.basicUserLoginData)
                       tokenManager.setNewToken(it.data.token)
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

}