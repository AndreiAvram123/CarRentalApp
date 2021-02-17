package com.andrei.engine.repository.interfaces

import androidx.lifecycle.LiveData
import com.andrei.carrental.entities.Message
import com.andrei.engine.DTOEntities.ChatDTO
import com.andrei.engine.State

interface ChatRepository {
    val userChats: LiveData<State<List<ChatDTO>>>
    suspend fun getInitialChatMessages(chatID: Long): List<Message>
    fun getLastChatMessage(chatID:Long):LiveData<Message>
}