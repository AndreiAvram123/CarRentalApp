package com.andrei.carrental.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.andrei.engine.State
import com.andrei.engine.repository.implementation.SignUpRepositoryImpl
import com.andrei.engine.repository.interfaces.PasswordValidationState
import com.andrei.engine.repository.interfaces.UsernameValidationState
import com.andrei.utils.isEmailValid
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

    private val _registrationState:MutableLiveData<State<Nothing>> by lazy {
        signUpRepo.registrationState
    }

    val registrationState:LiveData<State<Nothing>>
    get() = _registrationState


     val validationStateUsername :LiveData<UsernameValidationState> = Transformations.switchMap(_enteredUsername){
       signUpRepo.validateUsername(it).asLiveData()
    }

    val validationStatePassword:LiveData<PasswordValidationState> = Transformations.switchMap(_enteredPassword){
        signUpRepo.validatePassword(it).asLiveData()
    }

    val emailValid:LiveData<Boolean> = Transformations.map(_enteredEmail){
        it.isEmailValid()
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
            addSource(emailValid){
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
                viewModelScope.launch { signUpRepo.register(username = username,
                        email = email,
                        password = password)
                }
        }
    }

    private fun canStartRegistrationFlow():Boolean{
        return validationStateUsername.value is UsernameValidationState.Valid
                && validationStatePassword.value is PasswordValidationState.Valid
                && emailValid.value == true
                && reenteredPasswordValid.value == true
    }

}