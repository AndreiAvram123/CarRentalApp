package com.andrei.engine.repositoryInterfaces

import com.andrei.engine.DTOEntities.ChatDTO
import com.andrei.engine.configuration.CallWrapper
import com.andreia.carrental.requestModels.CreateMessageRequest
import retrofit2.http.*

interface ChatAPI {
    @GET("/users/{userID}/chats")
    fun getAllUserChats(@Path("userID") userID:Long):CallWrapper<List<ChatDTO>>

    @POST("/messages")
    fun postMessage(@Body requestBody: CreateMessageRequest): CallWrapper<Nothing>

    @PUT("/messages/{messageID}")
    fun modifyMessage(@Path("messageID") messageID:Long):CallWrapper<Nothing>
}