package com.andrei.engine.repository.interfaces

import kotlinx.coroutines.flow.Flow

interface SignUpRepository {
    fun getValidationErrorForUsername(username:String) : Flow<UsernameState>

}
sealed class UsernameState{
    object Valid : UsernameState()
    object AlreadyTaken: UsernameState()
    object  ErrorFormat:UsernameState()
}
