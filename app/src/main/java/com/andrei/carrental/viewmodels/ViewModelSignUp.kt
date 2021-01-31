package com.andrei.carrental.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.andrei.engine.repository.implementation.SignUpRepositoryImpl
import com.andrei.engine.repository.interfaces.UsernameState

class ViewModelSignUp @ViewModelInject constructor(
  private val signUpRepo: SignUpRepositoryImpl
) : ViewModel() {




    val enteredUsername :MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }


     val validationErrorUsername :LiveData<UsernameState> = Transformations.switchMap(enteredUsername){
       signUpRepo.getValidationErrorForUsername(it).asLiveData()
    }

}