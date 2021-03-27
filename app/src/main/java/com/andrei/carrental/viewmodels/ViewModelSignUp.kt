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



    private val _enteredUsername :MutableStateFlow<String> = MutableStateFlow("")


    private val _enteredEmail:MutableStateFlow<String> = MutableStateFlow("")

    private val _enteredPassword :MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    private val _reenteredPassword:MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    private val _registrationState:MutableStateFlow<RegistrationFlowState> = MutableStateFlow(RegistrationFlowState.Loading)

    val registrationState:StateFlow<RegistrationFlowState>
    get() = _registrationState.asStateFlow()

    private val _validationUsername:MutableStateFlow<State<UsernameValidationState>> = MutableStateFlow(State.Success(UsernameValidationState.Unvalidated))
    val validationUsername = _validationUsername.asStateFlow()

    private val _validationEmail:MutableStateFlow<State<EmailValidationState>> = MutableStateFlow(State.Success(EmailValidationState.Unvalidated))
    val validationEmail = _validationEmail.asStateFlow()




    val reenteredPasswordValid:LiveData<Boolean> = Transformations.map(_reenteredPassword){
        it == _enteredPassword.value
    }


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



    fun setEmail(email:String){
        viewModelScope.launch {
            _enteredEmail.emit(email)
        }
    }

    fun setPassword(password:String){
        _enteredPassword.postValue(password)
    }
    fun setReenteredPassword(reenteredPassword:String){
        _reenteredPassword.postValue(reenteredPassword)
    }



}