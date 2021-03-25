package com.andrei.engine.repository.implementation

import com.andrei.carrental.entities.User
import com.andrei.engine.APIs.UserAPI
import com.andrei.engine.CallRunner
import com.andrei.engine.DTOEntities.UserDTO
import com.andrei.engine.State
import com.andrei.engine.repository.interfaces.UserRepository
import com.andrei.engine.utils.mapState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val callRunner: CallRunner,
    private val userAPI:UserAPI
): UserRepository {

    override fun getUser(userID: Long): Flow<State<User>> =
        callRunner.makeApiCall {
            userAPI.getUser(userID)
        }.transform {
          val state:State<User> =  it.mapState(UserDTO::toUser)
           emit(state)
        }

}