package com.andrei.engine.repository.interfaces

import androidx.lifecycle.LiveData
import com.andrei.engine.DTOEntities.ChatDTO
import com.andrei.engine.State

interface ChatRepository {
    val userChats: LiveData<State<List<ChatDTO>>>
}