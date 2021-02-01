package com.andrei.engine.repositoryInterfaces

import com.andrei.carrental.entities.User
import com.andrei.engine.configuration.CallWrapper
import retrofit2.http.GET

interface UserRepoInterface {
  @GET("/users/{userID}")
  fun getUserByID(id:Int):CallWrapper<User>
}