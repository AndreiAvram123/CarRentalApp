package com.andrei.engine.repositoryInterfaces

import com.andrei.engine.DTOEntities.ChatDTO
import com.andrei.engine.configuration.CallWrapper
import com.andreia.carrental.requestModels.CreateMessageRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ChatAPI {
    @GET("/users/{userID}/chats")
    fun getAllUserChats(@Path("userID") userID:Long):CallWrapper<List<ChatDTO>>

    @POST("/chats/messages")
    fun postMessage(@Body requestBody: CreateMessageRequest): CallWrapper<Nothing>
}