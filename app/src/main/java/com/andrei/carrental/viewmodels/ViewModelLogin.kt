package com.andrei.carrental.viewmodels

import androidx.lifecycle.*
import com.andrei.engine.repository.interfaces.LoginRepository
import com.andrei.engine.states.LoginFlowState
import com.andrei.utils.isEmailValid
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModelAuth  @Inject constructor(
        private val loginRepository: LoginRepository
):   ViewModel() {


    private val _emailEntered : MutableStateFlow<String?> = MutableStateFlow(null)
    private val _passwordEntered : MutableStateFlow<String?>  = MutableStateFlow(null)






    val authenticationState:LiveData<AuthenticationState> = Transformations.map(loginRepository.loginFlowState.asLiveData(viewModelScope.coroutineContext)){
        when(it){
              is  LoginFlowState.Loading -> AuthenticationState.AUTHENTICATING
              is  LoginFlowState.LoginError -> AuthenticationState.NOT_AUTHENTICATED
               is LoginFlowState.LoggedIn -> AuthenticationState.AUTHENTICATED
               is LoginFlowState.NotLoggedIn -> AuthenticationState.NOT_AUTHENTICATED

        }
    }



    private val _errorEmail : Flow<String?>  = combine(_emailEntered.filterNotNull(),loginRepository.emailError ) {
        email, emailError ->
        when{
            !email.isEmailValid() -> errorInvalidEmailFormat
            !emailError.isNullOrBlank() -> emailError
            else -> null
        }
    }

    private val _errorPassword :Flow<String?> = combine(_passwordEntered.filterNotNull() ,loginRepository.passwordError){
        password,loginState ->
         when{
             password.isBlank() -> errorPasswordBlank
             !loginState.isNullOrBlank() ->loginState
             else-> null
         }

    }

    val errorEmail:LiveData<String?>
    get() = _errorEmail.asLiveData(viewModelScope.coroutineContext)

    val errorPassword:LiveData<String?>
    get() = _errorPassword.asLiveData(viewModelScope.coroutineContext)

    private val _startLoginFlow :Flow<Boolean> = combine(
            _errorEmail,_errorPassword
    ){
        errorE,errorP -> errorE == null && errorP == null
    }


    init {
        viewModelScope.launch {
           _startLoginFlow.collect {
                if (it) {
                    val email = _emailEntered.value
                    val password = _passwordEntered.value
                    check(email != null) { "Login flow should not be started with null email, the email might have been changed in another thread" }
                    check(password != null) { "Login flow should not be started with null password, the passwod might have been changed in another thread" }
                    loginRepository.startLoginFlow(email, password)
                }
            }
        }
    }


    fun signOut(){
        loginRepository.signOut()
    }

    fun setEmail(email:String){
        viewModelScope.launch {
            _emailEntered.emit(email)
        }
    }

    fun setPassword(password:String){
        _passwordEntered.tryEmit(password)
    }



  companion object{
      const val errorInvalidEmailFormat = "Invalid email format "
      const val errorPasswordBlank = "Please enter your password"


  }
      enum class AuthenticationState{
     AUTHENTICATED,AUTHENTICATING,NOT_AUTHENTICATED
    }


}