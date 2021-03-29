package com.andrei.engine.repository.implementation

import androidx.lifecycle.MutableLiveData
import com.andrei.engine.CallRunner
import com.andrei.engine.State
import com.andrei.engine.repository.interfaces.EmailValidationState
import com.andrei.engine.repository.interfaces.PasswordValidationState
import com.andrei.engine.repository.interfaces.SignUpRepository
import com.andrei.engine.repository.interfaces.UsernameValidationState
import com.andrei.engine.APIs.SignUpAPI
import com.andrei.engine.requestModels.RegisterRequest
import com.andrei.engine.states.RegistrationResponse
import com.andrei.engine.utils.mapState
import kotlinx.coroutines.flow.*
import javax.inject.Inject



class SignUpRepositoryImpl @Inject constructor(
        private val callRunner: CallRunner,
        private val signUpAPI: SignUpAPI
) : SignUpRepository {


    override val registrationState:MutableLiveData<RegistrationResponse> by lazy {
        MutableLiveData()
    }


    override fun validateUsername(username: String): Flow<State<UsernameValidationState>> =
            callRunner.makeApiCall { signUpAPI.checkIfUsernameIsAvailable(username) }
                .transform {
                    emit(it.mapState {data ->
                        when {
                            data.valid ->UsernameValidationState.Valid
                            data.reason == RegistrationResponse.RegistrationError.usernameAlreadyTaken ->
                                UsernameValidationState.AlreadyTaken

                            else -> UsernameValidationState.TooShort
                        }
                    })
                }

        override suspend fun validateEmail(email:String): Flow<State<EmailValidationState>> =
                callRunner.makeApiCall{signUpAPI.checkIfEmailIsAvailable(email)}.transform{state->
                emit(state.mapState {
                    when{
                        it.valid -> EmailValidationState.Valid
                        it.reason == RegistrationResponse.RegistrationError.emailAlreadyTaken -> EmailValidationState.AlreadyTaken
                        else -> EmailValidationState.InvalidFormat
                    }
                })
            }


        override fun validatePassword(password: String):Flow<State<PasswordValidationState>> = flow{
             val passwordValidation = if(password.length > 6){
                 PasswordValidationState.Valid
             }else{
                 PasswordValidationState.TooWeak
             }

              emit(State.Success(passwordValidation))
        }



        override  fun register(username:String, email:String, password: String, base64ProfilePicture:String): Flow<RegistrationResponse>{
            val registerRequest = RegisterRequest(
                username = username,
                email = email,
                password = password,
                base64ProfilePicture = base64ProfilePicture
            )
           return  callRunner.makeApiCall{signUpAPI.register(registerRequest)}.transform{
                emit(when(it){
                    is State.Success -> RegistrationResponse.Complete
                    is State.Error -> RegistrationResponse.RegistrationError.mapError(it.error)
                    else -> RegistrationResponse.Loading
                })
            }
        }

}

