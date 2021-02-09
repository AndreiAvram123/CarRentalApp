package com.andrei.engine.repository.implementation

import androidx.lifecycle.MutableLiveData
import com.andrei.engine.CallRunner
import com.andrei.engine.State
import com.andrei.engine.repository.interfaces.PasswordValidationState
import com.andrei.engine.repository.interfaces.SignUpRepository
import com.andrei.engine.repository.interfaces.UsernameValidationState
import com.andrei.engine.repositoryInterfaces.SignUpAPI
import com.andrei.engine.requestModels.RegisterRequest
import com.andrei.engine.states.RegistrationFlowState
import com.andrei.utils.isPasswordTooWeak
import com.andrei.utils.isUsernameInvalid
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject



class SignUpRepositoryImpl @Inject constructor(
        private val callRunner: CallRunner,
        private val signUpRepo: SignUpAPI
) : SignUpRepository {


    override val registrationState:MutableLiveData<RegistrationFlowState> by lazy {
        MutableLiveData()
    }


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

    override fun validatePassword(password: String):PasswordValidationState{
        if(password.isPasswordTooWeak()){
            return PasswordValidationState.TooWeak
        }
        return PasswordValidationState.Valid
    }



    override suspend fun register(username:String, email:String, password: String) {
        val registerRequest = RegisterRequest(
                username = username,
                email = email,
                password = password
        )
         callRunner.makeApiCall(signUpRepo.register(registerRequest)){
             when(it){
                 is State.Success -> registrationState.postValue(RegistrationFlowState.Finished)
                 is State.Loading -> registrationState.postValue(RegistrationFlowState.Loading)
                 is State.Error -> registrationState.postValue(RegistrationFlowState.RegistrationError.mapError(it.error))
             }
         }
    }


    companion object ErrorMessages{
        const val errorUsernameTaken  = "Username already taken"
    }
}

