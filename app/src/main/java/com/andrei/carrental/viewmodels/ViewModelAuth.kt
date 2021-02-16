package com.andrei.carrental.viewmodels

import androidx.lifecycle.*
import com.andrei.engine.repository.implementation.LoginRepositoryImpl
import com.andrei.engine.repository.interfaces.LoginRepository
import com.andrei.engine.states.LoginFlowState
import com.andrei.utils.isEmailValid
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModelAuth  @Inject constructor(
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
                 if(!it.isEmailValid()){
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
                if(it.isBlank()){
                    value = errorPasswordBlank
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




  companion object{
      const val errorInvalidEmailFormat = "Invalid email format "
      const val errorPasswordBlank = "Please enter your password"


  }
      enum class AuthenticationState{
     AUTHENTICATED,AUTHENTICATING,NOT_AUTHENTICATED
    }


}