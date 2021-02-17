package com.andrei.carrental.viewmodels

import androidx.lifecycle.*
import com.andrei.engine.repository.interfaces.LoginRepository
import com.andrei.engine.states.LoginFlowState
import com.andrei.utils.isEmailValid
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModelAuth  @Inject constructor(
        private val loginRepository: LoginRepository
):   ViewModel() {


    private val _emailEntered : MutableLiveData<String> by lazy {
        MutableLiveData()
    }
    val emailEntered:LiveData<String>
    get() = _emailEntered

    private val _passwordEntered : MutableLiveData<String> by lazy {
        MutableLiveData()
    }
    val passwordEntered:LiveData<String>
    get() = _passwordEntered





    val authenticationState:LiveData<AuthenticationState> = Transformations.map(loginRepository.loginFlowState){
        when(it){
              is  LoginFlowState.Loading -> AuthenticationState.AUTHENTICATING
              is  LoginFlowState.LoginError -> AuthenticationState.NOT_AUTHENTICATED
               is LoginFlowState.LoggedIn -> AuthenticationState.AUTHENTICATED
               is LoginFlowState.NotLoggedIn -> AuthenticationState.NOT_AUTHENTICATED

        }
    }



    val errorEmail : MediatorLiveData<String?> by lazy {
        MediatorLiveData<String?>().apply {
            addSource(_emailEntered){
                value = null
                 if(!it.isEmailValid()){
                   value =   errorInvalidEmailFormat
                }
            }
            addSource(loginRepository.loginFlowState){
                value = null
                if(it is LoginFlowState.LoginError.IncorrectEmail){
                    value = it.error
                }
            }
        }
    }

    val errorPassword :MediatorLiveData<String?> by lazy {
        MediatorLiveData<String?>().apply {
            addSource(_passwordEntered){
                value = null
                if(it.isBlank()){
                    value = errorPasswordBlank
                }
            }
            addSource(loginRepository.loginFlowState){
                value = null
                if(it is LoginFlowState.LoginError.IncorrectPassword){
                    value = it.error
                }
            }
        }
    }

    private val observerEmail = Observer<String> {
       val password= _passwordEntered.value
       if(it != null && password != null) {
           startLoginFlow(it,password)
       }
    }
    private val observerPassword = Observer<String>{
        val email = _emailEntered.value
        if(it != null && email != null) {
            startLoginFlow(email,it)
        }
    }

 init {
     _emailEntered.observeForever(observerEmail)
     _passwordEntered.observeForever(observerPassword)
 }


    override fun onCleared() {
        super.onCleared()
        _emailEntered.removeObserver(observerEmail)
        _passwordEntered.removeObserver(observerPassword)
    }

    private fun startLoginFlow(email:String, password: String){

        if(errorPassword.value == null
            && errorEmail.value == null){

           viewModelScope.launch {
               loginRepository.startLoginFlow(email = email,password = password)
           }
        }
    }

    fun signOut(){
        loginRepository.signOut()
    }

    fun setEmail(email:String){
        _emailEntered.postValue(email)
    }

    fun setPassword(password:String){
        _passwordEntered.value = (password)
    }



  companion object{
      const val errorInvalidEmailFormat = "Invalid email format "
      const val errorPasswordBlank = "Please enter your password"


  }
      enum class AuthenticationState{
     AUTHENTICATED,AUTHENTICATING,NOT_AUTHENTICATED
    }


}