package com.andrei.carrental.viewmodels

import androidx.lifecycle.*
import com.andrei.UI.FieldValidation
import com.andrei.engine.repository.interfaces.LoginRepository
import com.andrei.engine.states.LoginFlowState
import com.andrei.utils.isEmailInvalid
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModelLogin  @Inject constructor(
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


    private val _validationEmail:MutableStateFlow<FieldValidation> = MutableStateFlow(FieldValidation.Unvalidated)

    val errorEmail:StateFlow<FieldValidation>
    get() = _validationEmail


    private val _validationPassword:MutableStateFlow<FieldValidation> = MutableStateFlow(FieldValidation.Unvalidated)

    val errorPassword:StateFlow<FieldValidation>
    get() = _validationPassword


    private val passwordValidationFlow :Flow<FieldValidation> = combine(_passwordEntered.filterNotNull() ,loginRepository.loginFlowState){
            password,loginState ->
        when{
            password.isBlank() -> FieldValidation.Invalid(errorPasswordBlank)
            loginState is LoginFlowState.LoginError.IncorrectPassword ->FieldValidation.Invalid(loginState.error)
            else-> FieldValidation.Valid
        }
    }
    private val emailValidationFlow :Flow<FieldValidation> = combine(_emailEntered.filterNotNull(),loginRepository.loginFlowState ) {
            email, loginState ->
        when{
            email.isEmailInvalid() -> FieldValidation.Invalid(errorInvalidEmailFormat)
            loginState is LoginFlowState.LoginError.IncorrectEmail -> FieldValidation.Invalid(loginState.error)
            else -> FieldValidation.Valid
        }
    }





    fun signOut(){
        loginRepository.signOut()
    }

    init {
        viewModelScope.launch {
            emailValidationFlow.collect {
                _validationEmail.emit(it)
            }
        }
        viewModelScope.launch {
            passwordValidationFlow.collect {
                _validationPassword.emit(it)
            }
        }
    }

    fun login(email:String,password: String){
        viewModelScope.launch {
            _emailEntered.emit(email)
            _passwordEntered.emit(password)

            combine(
                emailValidationFlow, passwordValidationFlow
            ){
                    validationE,validationP-> validationE is FieldValidation.Valid && validationP is FieldValidation.Valid
            }.collectLatest {
                if (it) {
                    loginRepository.startLoginFlow(email, password)
                }
            }
        }
    }



  companion object{
      const val errorInvalidEmailFormat = "Invalid email format "
      const val errorPasswordBlank = "Please enter your password"


  }
      enum class AuthenticationState{
     AUTHENTICATED,AUTHENTICATING,NOT_AUTHENTICATED
    }


}