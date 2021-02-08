package com.andrei.engine.repository.implementation

import com.andrei.engine.CallRunner
import com.andrei.engine.State
import com.andrei.engine.repository.interfaces.PasswordValidationState
import com.andrei.engine.repository.interfaces.SignUpRepository
import com.andrei.engine.repository.interfaces.UsernameValidationState
import com.andrei.engine.repositoryInterfaces.SignUpRepositoryInterface
import com.andrei.utils.isPasswordTooShort
import com.andrei.utils.isUsernameInvalid
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SignUpRepositoryImpl @Inject constructor(
        private val callRunner: CallRunner,
        private val signUpRepo: SignUpRepositoryInterface
) : SignUpRepository {


    override fun validateUsername(username: String): Flow<UsernameValidationState> = flow {
        if(username.isUsernameInvalid()){
            emit(UsernameValidationState.ErrorFormat)
            return@flow
        }
        callRunner.makeApiCall(signUpRepo.checkIfUsernameIsAvailable(username)) {
            if (it is State.Success) {
                if (it.data != null) {
                    when {
                        it.data.usernameValid -> emit(UsernameValidationState.Valid)
                        it.data.reason == errorUsernameTaken -> emit(UsernameValidationState.AlreadyTaken)
                        else ->  emit(UsernameValidationState.ErrorFormat)
                    }
                }

            }
        }
    }

    override fun validatePassword(password: String): Flow<PasswordValidationState>  = flow{
        if(password.isPasswordTooShort()){
            emit(PasswordValidationState.TooShort)
            return@flow
        }
    }



    companion object ErrorMessages{
        const val errorUsernameTaken  = "Username already taken"
    }
}

