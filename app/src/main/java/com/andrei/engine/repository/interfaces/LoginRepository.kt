package com.andrei.engine.repository.interfaces

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.andrei.engine.DTOEntities.BasicUserLoginData
import com.andrei.engine.states.LoginFlowState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow


interface LoginRepository
{
    val loginFlowState : MutableStateFlow<LoginFlowState>
    suspend fun startLoginFlow(email:String,password:String)
}

