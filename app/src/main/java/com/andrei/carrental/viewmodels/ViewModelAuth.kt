package com.andrei.carrental.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.andrei.engine.repository.interfaces.AuthRepository
import com.andrei.engine.states.LoginError
import com.andrei.engine.states.LoginError.ErrorMessages.errorInvalidEmail
import com.andrei.engine.states.LoginError.ErrorMessages.errorInvalidPassword
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


    val errorEmail : MediatorLiveData<String?> by lazy {
        MediatorLiveData<String?>().apply {
            addSource(emailEntered){
                value = if(it.isUsernameValid()){
                    null
                }else{
                    errorInvalidUsernameFormat
                }
            }
         addSource(authRepository.loginFlowState){
             value = if(it is LoginError && it is LoginError.IncorrectEmail){
                 errorInvalidEmail
             }else{
                 null
             }
         }
        }

    }
    val errorPassword :MediatorLiveData<String?> by lazy {
        MediatorLiveData<String?>().apply {
            addSource(passwordEntered){
                value = if(it.isPasswordValid()){
                    null
                }else{
                    errorInvalidPasswordFormat
                }
            }
            addSource(authRepository.loginFlowState){
                value = if(it is LoginFlowState.Error && it is LoginError.IncorrectPassword ){
                        errorInvalidPassword
                }else{
                    null
                }
            }
        }
    }



    fun startLoginFlow(){
        val email=  emailEntered.value
        val password = passwordEntered.value

        if(errorPassword.value != null && errorEmail.value != null){
            check(email !=null){}
            check(password !=null){}

           viewModelScope.launch {
               authRepository.startLoginFlow(email,password)
           }
        }
    }

    private fun String.isUsernameValid():Boolean{
        return false
    }
    private fun String.isPasswordValid():Boolean{
        return false
    }


    //todo
    //put in strings

  companion object{
      const val errorInvalidUsernameFormat = "Invalid username format "
      const val errorInvalidPasswordFormat = "Invalid password format "

  }


}