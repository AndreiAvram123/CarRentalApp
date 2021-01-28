package com.andrei.engine.repository.interfaces

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.andrei.carrental.entities.UserAccount
import com.andrei.engine.DTOEntities.LoginResponse
import com.andrei.engine.states.LoginFlowState

interface AuthRepository {

    val isUserLoggedIn:MediatorLiveData<Boolean>
    val userAccount : LiveData<UserAccount>
    val loginFlowState : MutableLiveData<LoginFlowState>

    suspend fun startLoginFlow(email:String,password:String)

}

