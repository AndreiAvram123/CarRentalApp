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


    val errorEmail : LiveData<String?> = Transformations.map(emailEntered){
        if(it.isUsernameValid()){
            null
        }else{
            errorInvalidUsernameFormat
        }
    }
    val errorPassword :LiveData<String?> = Transformations.map(passwordEntered){
        if(it.isPasswordValid()){
            null
        }else{
            errorInvalidPasswordFormat
        }
    }


    val loginFlowState: LiveData<LoginFlowState> by lazy {
        authRepository.loginFlowState
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


  companion object{
      const val errorInvalidUsernameFormat = "Invalid username format "
      const val errorInvalidPasswordFormat = "Invalid password format "
  }


}