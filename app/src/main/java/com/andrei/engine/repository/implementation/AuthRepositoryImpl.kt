package com.andrei.engine.repository.implementation

import androidx.lifecycle.*
import com.andrei.carrental.entities.User
import com.andrei.engine.CallRunner
import com.andrei.engine.DTOEntities.BasicUser
import com.andrei.engine.State
import com.andrei.engine.helpers.TokenManager
import com.andrei.engine.helpers.TokenState
import com.andrei.engine.helpers.UserManager
import com.andrei.engine.repository.interfaces.AuthRepository
import com.andrei.engine.repositoryInterfaces.AuthRepoInterface
import com.andrei.engine.requestModels.LoginRequest
import com.andrei.engine.states.LoginFlowState
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val userManager: UserManager,
    private val tokenManager: TokenManager,
    private val authRepo: AuthRepoInterface,
    private val callRunner: CallRunner
): AuthRepository{

    override val isUserLoggedIn: MediatorLiveData<Boolean>  by lazy {
        MediatorLiveData<Boolean>().apply {
            addSource(user){
                if(it!=null && tokenManager.userToken.value is TokenState.Valid ){
                    value = true
                }
            }
            addSource(tokenManager.userToken){
                if(it is TokenState.Valid && user.value != null){
                    value = true
                }
            }
        }
    }

    override val user: LiveData<BasicUser> by lazy {
        userManager.user
    }

    override val loginFlowState: MutableLiveData<LoginFlowState> by lazy {
        MutableLiveData()
    }

    override suspend fun startLoginFlow(email: String, password: String) {
         //fetch user

        //fetch token
        val loginRequest = LoginRequest(
                email = email,
                password = password
        )
       callRunner.makeApiCall(authRepo.attemptLogin(loginRequest)){
           when(it){
               is State.Success -> {
                   loginFlowState.postValue(LoginFlowState.LoggedIn)
                   if (it.data != null) {
                       userManager.setNewUser(it.data.user)
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
                   }
               }
           }
       }

    }

}