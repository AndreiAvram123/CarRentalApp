package com.andrei.carrental.viewmodels

import androidx.lifecycle.*
import com.andrei.engine.State
import com.andrei.engine.repository.implementation.SignUpRepositoryImpl
import com.andrei.engine.repository.interfaces.EmailValidationState
import com.andrei.engine.repository.interfaces.PasswordValidationState
import com.andrei.engine.repository.interfaces.UsernameValidationState
import com.andrei.engine.states.RegistrationFlowState
import com.andrei.utils.isUsernameTooShort
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


    val validationStatePassword:MediatorLiveData<PasswordValidationState> by lazy {
        MediatorLiveData<PasswordValidationState>().apply {
            addSource(_enteredPassword){
                    value = signUpRepo.validatePassword(it)
            }
            addSource(_registrationState.asLiveData()){
                if(it is RegistrationFlowState.RegistrationError.PasswordTooWeak){
                    value = PasswordValidationState.TooWeak
                }
            }
        }
    }

    val validationStateEmail:MediatorLiveData<EmailValidationState> by lazy {
        MediatorLiveData<EmailValidationState>().apply {
            addSource(_enteredEmail.asLiveData()){
               viewModelScope.launch {
                   value = signUpRepo.validateEmail(it)
               }
            }
            addSource(registrationState.asLiveData()){
                if(it is RegistrationFlowState.RegistrationError.EmailAlreadyTaken){
                    value = EmailValidationState.AlreadyTaken
                }
            }
        }
    }
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