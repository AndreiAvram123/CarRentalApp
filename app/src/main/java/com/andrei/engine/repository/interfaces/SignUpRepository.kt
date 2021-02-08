package com.andrei.engine.repository.interfaces

import androidx.lifecycle.MutableLiveData
import com.andrei.engine.State
import com.andrei.engine.requestModels.RegisterRequest
import kotlinx.coroutines.flow.Flow

interface SignUpRepository {
    fun validateUsername(username:String) : Flow<UsernameValidationState>
    fun validatePassword(password:String) : Flow<PasswordValidationState>
    suspend fun register(username: String, email: String, password: String)
    val registrationState: MutableLiveData<State<Nothing>>
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
