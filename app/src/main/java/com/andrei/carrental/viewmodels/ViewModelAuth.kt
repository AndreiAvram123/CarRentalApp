package com.andrei.carrental.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.andrei.engine.repository.interfaces.AuthRepository
import com.andrei.engine.states.LoginFlowState
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

class ViewModelAuth  @ViewModelInject constructor(
        private val authRepository: AuthRepository
):   ViewModel() {



    val isUserLoggedIn :LiveData<Boolean>  = authRepository.isUserLoggedIn


    val usernameEntered : MutableLiveData<String> by lazy {
        MutableLiveData()
    }
    val passwordEntered : MutableLiveData<String> by lazy {
        MutableLiveData()
    }


    val errorUsername : LiveData<String?> = Transformations.map(usernameEntered){
        if(it.isUsernameValid()){
            null
        }else{
            "pupu"
        }
    }
    val errorPassword :LiveData<String?> = Transformations.map(passwordEntered){
        if(it.isPasswordValid()){
            null
        }else{
            "ow no"
        }
    }




    val loginFlowState: LiveData<LoginFlowState> by lazy {
        authRepository.loginState
    }

    fun startLoginFlow(){
        if(errorPassword.value == null && errorUsername.value == null){

        }
    }

    private fun String.isUsernameValid():Boolean{
        return false
    }
    private fun String.isPasswordValid():Boolean{
        return false
    }





}