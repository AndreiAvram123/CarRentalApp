package com.andrei.carrental.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.andrei.engine.repository.interfaces.AuthRepository
import com.andrei.engine.states.LoginFlowState
import kotlinx.coroutines.launch

class ViewModelAuth  @ViewModelInject constructor(
        private val authRepository: AuthRepository
):   ViewModel() {



    val isUserLoggedIn :LiveData<Boolean>  = authRepository.isUserLoggedIn


    val emailEntered : MutableLiveData<String> by lazy {
        MutableLiveData()
    }
    val passwordEntered : MutableLiveData<String> by lazy {
        MutableLiveData()
    }

    val isAuthenticationInProgress : LiveData<Boolean> = Transformations.map(authRepository.loginFlowState){
        it is LoginFlowState.Loading
    }

    val errorEmail : MediatorLiveData<String?> = MediatorLiveData<String?>().apply {
            addSource(emailEntered){
                value = if(it.isEmailValid()){
                    null
                }else{
                    errorInvalidEmailFormat
                }
            }
         addSource(authRepository.loginFlowState){
             value = if(it is LoginFlowState.LoginError && it is LoginFlowState.LoginError.IncorrectEmail){
                 it.error
             }else{
                 null
             }
         }

    }
    val errorPassword :MediatorLiveData<String?> = MediatorLiveData<String?>().apply {
            addSource(passwordEntered){
                value = if(it.isPasswordValid()){
                    null
                }else{
                    errorInvalidPasswordFormat
                }
            }
            addSource(authRepository.loginFlowState){
                value = if(it is LoginFlowState.LoginError && it is LoginFlowState.LoginError.IncorrectPassword ){
                        it.error
                }else{
                    null
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
               authRepository.startLoginFlow(email = email,password = password)
           }
        }
    }

    private fun String.isEmailValid():Boolean{
        return this.length > 10

    }
    private fun String.isPasswordValid():Boolean{
        return this.length > 10
    }


  companion object{
      const val errorInvalidEmailFormat = "Invalid email format "
      const val errorInvalidPasswordFormat = "Invalid password format "

  }


}