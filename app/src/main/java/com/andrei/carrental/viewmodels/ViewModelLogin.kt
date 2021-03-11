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



    fun signOut(){
        loginRepository.signOut()
    }

    init {
        viewModelScope.launch {
            loginRepository.loginFlowState.collect {
                when{
                    it is LoginFlowState.LoginError.IncorrectEmail -> _validationEmail.emit(FieldValidation.Invalid(it.error))
                    it is LoginFlowState.LoginError.IncorrectPassword -> _validationPassword.emit(FieldValidation.Invalid(it.error))
                }
            }
        }

    }

    fun login(email:String,password: String){
        viewModelScope.launch {
            _validationEmail.emit(FieldValidation.Valid)
            _validationPassword.emit(FieldValidation.Valid)
            when {
                email.isEmailInvalid() ->
                    _validationEmail.emit(FieldValidation.Invalid(errorInvalidEmailFormat))
                password.isBlank() -> FieldValidation.Invalid(errorPasswordBlank)
                else -> {
                    loginRepository.startLoginFlow(email = email ,password = password)
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