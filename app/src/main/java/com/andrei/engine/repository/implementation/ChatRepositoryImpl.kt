package com.andrei.engine.repository.implementation

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.liveData
import com.andrei.carrental.entities.Chat
import com.andrei.carrental.room.dao.MessageDao
import com.andrei.engine.CallRunner
import com.andrei.engine.DTOEntities.ChatDTO
import com.andrei.engine.DTOEntities.toMessage
import com.andrei.engine.State
import com.andrei.engine.helpers.UserManager
import com.andrei.engine.repository.interfaces.ChatRepository
import com.andrei.engine.repositoryInterfaces.ChatAPI
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
        private val userManager: UserManager,
        private val callRunner: CallRunner,
        private val chatAPI: ChatAPI,
        private val messageDao: MessageDao

): ChatRepository {

    override val userChats: LiveData<State<List<ChatDTO>>> = Transformations.switchMap(userManager.userLoginData){
       fetchUserChats(it.id)
    }

    private fun fetchUserChats(userID:Int):LiveData<State<List<ChatDTO>>> = liveData{
      callRunner.makeApiCall(chatAPI.getAllUserChats(userID)){
          if(it is State.Success){
              //insert into room
               if(it.data != null){
                    it.data.forEach { chatDTO->
                        val messages = chatDTO.lastMessages.map {messageDTO ->  messageDTO.toMessage() }
                        messageDao.insertMessages(messages)
                    }
               }
          }
          emit(it)
      }
    }
}