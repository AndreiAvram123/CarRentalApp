package com.andrei.engine.repository.interfaces

import kotlinx.coroutines.flow.Flow

interface SignUpRepository {
    fun validateUsername(username:String) : Flow<UsernameValidationState>
    fun validatePassword(password:String) : Flow<PasswordValidationState>

}
sealed class UsernameValidationState{
    object Valid : UsernameValidationState()
    object AlreadyTaken: UsernameValidationState()
    object  ErrorFormat:UsernameValidationState()
}

sealed class PasswordValidationState{
    object Valid:PasswordValidationState()
    object TooShort:PasswordValidationState()
    object TooWeak:PasswordValidationState()
}
