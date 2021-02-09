package com.andrei.carrental.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.andrei.engine.repository.implementation.SignUpRepositoryImpl
import com.andrei.engine.repository.interfaces.EmailValidationState
import com.andrei.engine.repository.interfaces.PasswordValidationState
import com.andrei.engine.repository.interfaces.UsernameValidationState
import com.andrei.engine.states.RegistrationFlowState
import kotlinx.coroutines.launch

class ViewModelSignUp @ViewModelInject constructor(
  private val signUpRepo: SignUpRepositoryImpl
) : ViewModel() {



    private val _enteredUsername :MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    private val _enteredEmail:MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    private val _enteredPassword :MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    private val _reenteredPassword:MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    private val _registrationState:MutableLiveData<RegistrationFlowState> by lazy {
        signUpRepo.registrationState
    }

    val registrationState:LiveData<RegistrationFlowState>
    get() = _registrationState


     val validationStateUsername :MediatorLiveData<UsernameValidationState> by lazy {
         MediatorLiveData<UsernameValidationState>().apply {
             addSource(_enteredUsername){
                 viewModelScope.launch {
                     value = signUpRepo.validateUsername(it)
                 }
             }
             addSource(registrationState){
                 if(it is RegistrationFlowState.RegistrationError.UsernameAlreadyTaken){
                     value = UsernameValidationState.AlreadyTaken
                 }
             }
         }
     }

    val validationStatePassword:MediatorLiveData<PasswordValidationState> by lazy {
        MediatorLiveData<PasswordValidationState>().apply {
            addSource(_enteredPassword){
                    value = signUpRepo.validatePassword(it)
            }
            addSource(_registrationState){
                if(it is RegistrationFlowState.RegistrationError.PasswordTooWeak){
                    value = PasswordValidationState.TooWeak
                }
            }
        }
    }

    val validationStateEmail:MediatorLiveData<EmailValidationState> by lazy {
        MediatorLiveData<EmailValidationState>().apply {
            addSource(_enteredEmail){
               viewModelScope.launch {
                   value = signUpRepo.validateEmail(it)
               }
            }
            addSource(registrationState){
                if(it is RegistrationFlowState.RegistrationError.EmailAlreadyTaken){
                    value = EmailValidationState.AlreadyTaken
                }
            }
        }
    }
    val reenteredPasswordValid:LiveData<Boolean> = Transformations.map(_reenteredPassword){
        it == _enteredPassword.value
    }

    private val _registrationFlowEnabled:MediatorLiveData<Boolean> by lazy {
        MediatorLiveData<Boolean>().apply {
            value  = false
            addSource(validationStateUsername){
               value = canStartRegistrationFlow()
            }
            addSource(validationStateEmail){
                value = canStartRegistrationFlow()
            }
            addSource(validationStatePassword){
                value = canStartRegistrationFlow()
            }
            addSource(reenteredPasswordValid){
                value = canStartRegistrationFlow()
            }

        }
    }
    val registrationFlowEnabled:LiveData<Boolean>
    get() = _registrationFlowEnabled



    fun setUsername(username:String){
        _enteredUsername.postValue(username)
    }
    fun setEmail(email:String){
        _enteredEmail.postValue(email)
    }

    fun setPassword(password:String){
        _enteredPassword.postValue(password)
    }
    fun setReenteredPassword(reenteredPassword:String){
        _reenteredPassword.postValue(reenteredPassword)
    }



    fun startRegistrationFlow() {
        if (canStartRegistrationFlow()) {
            val username = _enteredUsername.value
            val password = _enteredPassword.value
            val email = _enteredEmail.value
            check(username != null) {}
            check(password != null) {}
            check(email != null) {}
                viewModelScope.launch {
                    signUpRepo.register(username = username,
                        email = email,
                        password = password)
                }
        }
    }

    private fun canStartRegistrationFlow():Boolean{
        return validationStateUsername.value is UsernameValidationState.Valid
                && validationStatePassword.value is PasswordValidationState.Valid
                && validationStateEmail.value  is EmailValidationState.Valid
                && reenteredPasswordValid.value == true
    }

}