package com.andrei.carrental.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.andrei.engine.repository.implementation.SignUpRepositoryImpl
import com.andrei.engine.repository.interfaces.PasswordValidationState
import com.andrei.engine.repository.interfaces.UsernameValidationState
import com.andrei.utils.isEmailInvalid

class ViewModelSignUp @ViewModelInject constructor(
  private val signUpRepo: SignUpRepositoryImpl
) : ViewModel() {



    private val _enteredUsername :MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    private val _enteredPassword :MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    private val _enteredEmail:MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }


     val validationStateUsername :LiveData<UsernameValidationState> = Transformations.switchMap(_enteredUsername){
       signUpRepo.validateUsername(it).asLiveData()
    }

    val validationStatePassword:LiveData<PasswordValidationState> = Transformations.switchMap(_enteredPassword){
        signUpRepo.validatePassword(it).asLiveData()
    }

    val emailInvalid:LiveData<Boolean> = Transformations.map(_enteredEmail){
        it.isEmailInvalid()
    }


    fun setUsername(username:String){
        _enteredUsername.postValue(username)
    }

    fun setPassword(password:String){
        _enteredPassword.postValue(password)
    }
    fun setEmail(email:String){
        _enteredEmail.postValue(email)
    }




}