package com.andrei.engine.repository.implementation

import com.andrei.engine.CallRunner
import com.andrei.engine.repository.interfaces.SignUpRepository
import com.andrei.engine.repository.interfaces.SignUpRepositoryInterface
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class SignUpRepositoryImpl @Inject constructor(
        private val callRunner: CallRunner,
        private val singUpRepo:SignUpRepositoryInterface
) : SignUpRepository {

    override fun validateUsername(username:String) : Flow<String?>  = flow{
        emit("baddddd")
    }

}