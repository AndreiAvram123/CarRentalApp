package com.andrei.engine.repository.implementation

import com.andrei.engine.CallRunner
import com.andrei.engine.State
import com.andrei.engine.repository.interfaces.SignUpRepository
import com.andrei.engine.repositoryInterfaces.SignUpRepositoryInterface
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SignUpRepositoryImpl @Inject constructor(
        private val callRunner: CallRunner,
        private val signUpRepo: SignUpRepositoryInterface
) : SignUpRepository {


    override fun getValidationErrorForUsername(username: String): Flow<String?> = flow {
        callRunner.makeApiCall(signUpRepo.checkIfUsernameIsAvailable(username)) {
            if (it is State.Success) {
                if (it.data != null) {
                    when {
                        it.data.usernameValid -> emit(null)
                        it.data.reason != null -> emit(it.data.reason)
                        else -> emit("Something is wrong with this username")
                    }
                }

            }
        }
    }
}

