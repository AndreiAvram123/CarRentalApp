package com.andrei.engine.repository.interfaces

import com.andrei.carrental.entities.User
import com.andrei.engine.State
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getUser(userID:Long): Flow<State<User>>
}