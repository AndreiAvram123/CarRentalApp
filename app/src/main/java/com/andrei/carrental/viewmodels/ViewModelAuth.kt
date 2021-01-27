package com.andrei.carrental.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.andrei.engine.repository.interfaces.AuthRepository
import com.andrei.engine.states.LoginFlowState

class ViewModelAuth  constructor(
        private val authRepository: AuthRepository
):   ViewModel() {

    val isUserLoggedIn :LiveData<Boolean> = Transformations.map(authRepository.userAccountDetails) {
         it !=null
    }

    val loginFlowState: LiveData<LoginFlowState> by lazy {
        authRepository.loginState
    }


}