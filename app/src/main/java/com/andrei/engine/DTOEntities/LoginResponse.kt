package com.andrei.engine.DTOEntities

import com.andrei.carrental.entities.User


data class LoginResponse(
        val user:User,
        val token :String
)
