package com.andrei.carrental.viewmodels

import androidx.lifecycle.*
import com.andrei.engine.State
import com.andrei.engine.repository.implementation.SignUpRepositoryImpl
import com.andrei.engine.repository.interfaces.EmailValidationState
import com.andrei.engine.repository.interfaces.PasswordValidationState
import com.andrei.engine.repository.interfaces.UsernameValidationState
import com.andrei.engine.states.RegistrationFlowState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModelSignUp @Inject constructor(
        private val signUpRepo: SignUpRepositoryImpl
) : ViewModel() {



    private val _enteredUsername :MutableStateFlow<String> by lazy {
        MutableStateFlow("")
    }


    private val _enteredEmail:MutableStateFlow<String> by lazy {
        MutableStateFlow("")
    }


    private val _enteredPassword :MutableStateFlow<String> by lazy {
        MutableStateFlow("")
    }


    private val _reenteredPassword:MutableStateFlow<String> by lazy {
        MutableStateFlow("")
    }

    private val _registrationState:MutableStateFlow<RegistrationFlowState> = MutableStateFlow(RegistrationFlowState.Loading)


    private val _validationUsername:MutableStateFlow<State<UsernameValidationState>> by lazy {
        MutableStateFlow(State.Success(UsernameValidationState.Unvalidated))
    }
    val validationUsername = _validationUsername.asStateFlow()

    private val _validationEmail:MutableStateFlow<State<EmailValidationState>> by lazy{
        MutableStateFlow(State.Success(EmailValidationState.Unvalidated))
    }
    val validationEmail = _validationEmail.asStateFlow()

    private val _validationPassword:MutableStateFlow<State<PasswordValidationState>> by lazy { MutableStateFlow(State.Success(PasswordValidationState.Unvalidated)) }
    val validationPassword = _validationPassword.asStateFlow()




    fun setUsername(username:String){
        viewModelScope.launch {
            _enteredUsername.emit(username)
        }
    }
    fun validateUsername() {
        viewModelScope.launch {
            val username = _enteredUsername.value
            signUpRepo.validateUsername(username).collect {
                _validationUsername.emit(it)
            }
        }
    }
    fun validateEmail(){
        viewModelScope.launch {
            val email = _enteredEmail.value
            signUpRepo.validateEmail(email).collect {
                _validationEmail.emit(it)
            }
        }
    }
    fun validatePassword(){
        viewModelScope.launch {
            val password = _enteredPassword.value
            signUpRepo.validatePassword(password).collect {
                _validationPassword.emit(it)
            }
        }
    }




    fun setEmail(email:String) = viewModelScope.launch {
            _enteredEmail.emit(email)
    }

    fun setPassword(password:String)= viewModelScope.launch {
            _enteredPassword.emit(password)
        }

    fun setReenteredPassword(reenteredPassword:String) = viewModelScope.launch {
        _reenteredPassword.emit(reenteredPassword)
    }



}