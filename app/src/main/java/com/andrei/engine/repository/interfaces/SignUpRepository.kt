package com.andrei.engine.repository.interfaces

import com.andrei.engine.repository.implementation.SignUpRepositoryImpl
import kotlinx.coroutines.flow.Flow

interface SignUpRepository {
    fun getValidationErrorForUsername(username:String) : Flow<String?>
}
