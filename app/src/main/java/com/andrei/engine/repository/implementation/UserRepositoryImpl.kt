package com.andrei.engine.repository.implementation

import com.andrei.carrental.entities.User
import com.andrei.engine.APIs.UserAPI
import com.andrei.engine.CallRunner
import com.andrei.engine.DTOEntities.UserDTO
import com.andrei.engine.DTOEntities.toUser
import com.andrei.engine.State
import com.andrei.engine.repository.interfaces.UserRepository
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

    @Suppress("UNCHECKED_CAST")
    fun <T,R> State<T>.mapState(transformation: (data:T) -> R):State<R>{
       return when(this){
            is State.Success ->  State.Success(transformation(this.data))
            is State.Loading ->  State.Loading
            is State.Error -> State.Error(this.error)
            is State.Default -> State.Default
        }
    }
}