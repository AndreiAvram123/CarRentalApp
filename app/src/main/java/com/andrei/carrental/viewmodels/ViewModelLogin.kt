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


    private val _loginFlowState:MutableStateFlow<LoginFlowState> = loginRepository.loginFlowState

    val loginFlowState:StateFlow<LoginFlowState>
    get() = _loginFlowState

    private val _validationEmail:MutableStateFlow<FieldValidation> = MutableStateFlow(FieldValidation.Unvalidated)

    val errorEmail:StateFlow<FieldValidation>
    get() = _validationEmail


    private val _validationPassword:MutableStateFlow<FieldValidation> = MutableStateFlow(FieldValidation.Unvalidated)

    val errorPassword:StateFlow<FieldValidation>
    get() = _validationPassword


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
                password.isBlank() -> _validationPassword.emit(FieldValidation.Invalid(errorPasswordBlank))
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


}