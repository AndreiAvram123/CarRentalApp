package com.andrei.carrental.viewmodels

import android.graphics.drawable.Drawable
import androidx.lifecycle.*
import com.andrei.engine.State
import com.andrei.engine.repository.implementation.SignUpRepositoryImpl
import com.andrei.engine.repository.interfaces.EmailValidationState
import com.andrei.engine.repository.interfaces.PasswordValidationState
import com.andrei.engine.repository.interfaces.UsernameValidationState
import com.andrei.engine.states.RegistrationResponse
import com.andrei.utils.toBase64
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
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

    private val _profilePictureBase64:MutableStateFlow<String> by lazy {
        MutableStateFlow("")
    }

    private val _registrationState:MutableStateFlow<RegistrationResponse> = MutableStateFlow(RegistrationResponse.Loading)
    val registrationState = _registrationState.asStateFlow()


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
            validateUsername()
        }
    }
    private fun validateUsername() {
        viewModelScope.launch {
            val username = _enteredUsername.value
            signUpRepo.validateUsername(username).collect {
                _validationUsername.emit(it)
            }
        }
    }
    private fun validateEmail(){
        viewModelScope.launch {
            val email = _enteredEmail.value
            signUpRepo.validateEmail(email).collect {
                _validationEmail.emit(it)
            }
        }
    }
    private fun validatePassword(){
        viewModelScope.launch {
            val password = _enteredPassword.value
            signUpRepo.validatePassword(password).collect {
                _validationPassword.emit(it)
            }
        }
    }


    init {
        viewModelScope.launch {
            combine(_enteredUsername, _enteredEmail, _enteredPassword, _profilePictureBase64) { username, email, password, profilePicture ->
                return@combine username.isNotBlank() && email.isNotBlank() && password.isNotBlank() && profilePicture.isNotBlank()
            }.collectLatest {
                if (it) {
                    registerUser()
                }
            }
        }
    }

    private fun registerUser(){
        viewModelScope.launch {
            signUpRepo.register(username = _enteredUsername.value, email = _enteredEmail.value, password = _enteredPassword.value,
                    base64ProfilePicture = _profilePictureBase64.value).collect {
                when(it){
                    is RegistrationResponse.Complete -> clear()
                }
               _registrationState.emit(it)
            }
        }
    }

    private fun clear(){
        viewModelScope.launch {
            _enteredUsername.clean()
            _enteredEmail.clean()
            _enteredPassword.clean()
            _profilePictureBase64.clean()
        }
    }




    fun setEmail(email:String) = viewModelScope.launch {
            _enteredEmail.emit(email)
            validateEmail()
    }

    fun setPassword(password:String)= viewModelScope.launch {
            _enteredPassword.emit(password)
             validatePassword()
        }


    fun setProfilePicture(drawable:Drawable)= viewModelScope.launch(Dispatchers.IO) {
            val base64String = drawable.toBase64()
            _profilePictureBase64.emit(base64String)
        }

   private suspend fun MutableStateFlow<String>.clean(){
       emit("")
   }

}