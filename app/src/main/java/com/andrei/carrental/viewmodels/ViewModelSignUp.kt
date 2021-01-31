package com.andrei.carrental.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.andrei.engine.repository.implementation.SignUpRepositoryImpl

class ViewModelSignUp @ViewModelInject constructor(
  private val signUpRepo: SignUpRepositoryImpl
) : ViewModel() {




    val enteredUsername :MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }


     val validationErrorUsername :LiveData<String?> = Transformations.switchMap(enteredUsername){
       signUpRepo.getValidationErrorForUsername(it).asLiveData()
    }

}