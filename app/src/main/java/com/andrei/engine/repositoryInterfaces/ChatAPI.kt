package com.andrei.engine.repositoryInterfaces

import com.andrei.engine.DTOEntities.ChatDTO
import com.andrei.engine.configuration.APIResponse
import com.andrei.engine.configuration.CallWrapper
import com.andreia.carrental.requestModels.CreateMessageRequest
import retrofit2.Response
import retrofit2.http.*

interface ChatAPI {
    @GET("/users/{userID}/chats")
    suspend fun getAllUserChats(@Path("userID") userID:Long):Response<APIResponse<List<ChatDTO>>>

    @POST("/messages")
    suspend fun postMessage(@Body requestBody: CreateMessageRequest): Response<APIResponse<Nothing>>

    @PUT("/messages/{messageID}")
    suspend  fun modifyMessage(@Path("messageID") messageID:Long):Response<APIResponse<Nothing>>
}