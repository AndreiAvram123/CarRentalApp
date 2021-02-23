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

    override val emailError:MutableStateFlow<String?> = MutableStateFlow(null)
    override val passwordError:MutableStateFlow<String?> = MutableStateFlow(null)

    init {
//        GlobalScope.launch {
//        userManager.userLogedInObserver.asFlow().filterNotNull() .collect {
//            if(it){
//                loginFlowState.tryEmit(LoginFlowState.LoggedIn)
//            }else{
//                loginFlowState.tryEmit(LoginFlowState.NotLoggedIn)
//            }
//        }
//        }
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
                       loginFlowState.tryEmit(LoginFlowState.LoggedIn)
                   }
               }
               is State.Loading -> {
                   loginFlowState.tryEmit(LoginFlowState.Loading)
               }
               is State.Error->{
                   when(it.error){
                        LoginFlowState.LoginError.errorInvalidEmail -> loginFlowState.tryEmit(LoginFlowState.LoginError.IncorrectEmail)
                        LoginFlowState.LoginError.errorInvalidPassword -> loginFlowState.tryEmit(LoginFlowState.LoginError.IncorrectPassword)
                        else -> loginFlowState.tryEmit(LoginFlowState.LoginError.ConnectionError)
                   }
               }
           }
       }

    }

    override fun signOut() {
        userManager.signOut()
    }

}