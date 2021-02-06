package com.andrei.carrental.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.andrei.engine.repository.interfaces.LoginRepository
import com.andrei.engine.states.LoginFlowState
import kotlinx.coroutines.launch

class ViewModelAuth  @ViewModelInject constructor(
        private val loginRepository: LoginRepository
):   ViewModel() {


    val emailEntered : MutableLiveData<String> by lazy {
        MutableLiveData()
    }
    val passwordEntered : MutableLiveData<String> by lazy {
        MutableLiveData()
    }




    val authenticationState:LiveData<AuthenticationState> = Transformations.map(loginRepository.loginFlowState){
        when(it){
              is  LoginFlowState.Loading -> AuthenticationState.AUTHENTICATING
              is  LoginFlowState.LoginError -> AuthenticationState.NOT_AUTHENTICATED
               is LoginFlowState.LoggedIn -> AuthenticationState.AUTHENTICATED
               is LoginFlowState.NotLoggedIn -> AuthenticationState.NOT_AUTHENTICATED

        }
    }



    val errorEmail : MediatorLiveData<String?> by lazy {
        MediatorLiveData<String?>().apply {
            addSource(emailEntered){
                value = null
                if(it.isEmailInvalid()){
                  value =   errorInvalidEmailFormat
                }
            }
            addSource(loginRepository.loginFlowState){
                value = null
                if(it is LoginFlowState.LoginError.IncorrectEmail){
                    value = it.error
                }
            }
        }
    }

    val errorPassword :MediatorLiveData<String?> by lazy {
        MediatorLiveData<String?>().apply {
            addSource(passwordEntered){
                value = null
                if(it.isPasswordInvalid()){
                    value = errorInvalidPasswordFormat
                }
            }
            addSource(loginRepository.loginFlowState){
                value = null
                if(it is LoginFlowState.LoginError.IncorrectPassword){
                    value = it.error
                }
            }
        }
    }



    fun startLoginFlow(){
        val email=  emailEntered.value
        val password = passwordEntered.value

        if(errorPassword.value == null && errorEmail.value == null){
            check(email !=null){}
            check(password !=null){}

           viewModelScope.launch {
               loginRepository.startLoginFlow(email = email,password = password)
           }
        }
    }

    fun signOut(){
        loginRepository.signOut()
    }


    private fun String.isEmailInvalid():Boolean{
        return this.length < 10

    }
    private fun String.isPasswordInvalid():Boolean{
        return this.length < 6
    }


  companion object{
      const val errorInvalidEmailFormat = "Invalid email format "
      const val errorInvalidPasswordFormat = "Invalid password format "


  }
      enum class AuthenticationState{
     AUTHENTICATED,AUTHENTICATING,NOT_AUTHENTICATED
    }


}