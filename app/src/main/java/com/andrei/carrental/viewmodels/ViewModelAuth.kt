package com.andrei.carrental.viewmodels

import androidx.lifecycle.*
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


    private val _emailEntered : MutableLiveData<String> by lazy {
        MutableLiveData()
    }
    private val _passwordEntered : MutableLiveData<String> by lazy {
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
            addSource(_emailEntered){
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
            addSource(_passwordEntered){
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

    private val observerEmail = Observer<String> {
        if(canStartUserFlow()){
            startLoginFlow()
        }
    }
    private val observerPassword = Observer<String>{
        if(canStartUserFlow()){
            startLoginFlow()
        }
    }

 init {
     _emailEntered.observeForever(observerEmail)
     _passwordEntered.observeForever(observerPassword)
 }


    private fun canStartUserFlow():Boolean{
        val email = _emailEntered.value
        val password = _passwordEntered.value
        val errorEmail = errorEmail.value
        val errorPassword = errorPassword.value
       return  email != null && password != null && errorEmail == null && errorPassword == null
    }

    private fun startLoginFlow(){
         val email = _emailEntered.value
        val password = _passwordEntered.value
        if(canStartUserFlow()) {
            check(email != null) { "Login flow should not be started with null email, the email might have been changed in another thread" }
            check(password != null) { "Login flow should not be started with null password, the passwod might have been changed in another thread" }
                viewModelScope.launch {
                    loginRepository.startLoginFlow(email = email, password = password)
            }
        }
    }


    override fun onCleared() {
        super.onCleared()
        _emailEntered.removeObserver(observerEmail)
        _passwordEntered.removeObserver(observerPassword)
    }



    fun signOut(){
        loginRepository.signOut()
    }

    fun setEmail(email:String){
        _emailEntered.value = email
    }

    fun setPassword(password:String){
        _passwordEntered.value = (password)
    }



  companion object{
      const val errorInvalidEmailFormat = "Invalid email format "
      const val errorPasswordBlank = "Please enter your password"


  }
      enum class AuthenticationState{
     AUTHENTICATED,AUTHENTICATING,NOT_AUTHENTICATED
    }


}