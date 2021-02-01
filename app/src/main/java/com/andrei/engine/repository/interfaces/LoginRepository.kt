package com.andrei.engine.repository.interfaces

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.andrei.engine.DTOEntities.BasicUserLoginData
import com.andrei.engine.states.LoginFlowState

interface LoginRepository {

    val isUserLoggedIn:MediatorLiveData<Boolean>
    val userLoginData : LiveData<BasicUserLoginData>
    val loginFlowState : MutableLiveData<LoginFlowState>

    suspend fun startLoginFlow(email:String,password:String)

}

