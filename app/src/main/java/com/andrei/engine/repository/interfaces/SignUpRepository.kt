package com.andrei.engine.repository.interfaces

import kotlinx.coroutines.flow.Flow

interface SignUpRepository {
    fun validateUsername(username:String) : Flow<String?>
}