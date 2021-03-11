package com.andrei.engine.repository.implementation

import com.andrei.carrental.UserDataManager
import com.andrei.carrental.entities.Message
import com.andrei.carrental.room.dao.MessageDao
import com.andrei.engine.CallRunner
import com.andrei.engine.DTOEntities.ChatDTO
import com.andrei.engine.DTOEntities.toMessage
import com.andrei.engine.State
import com.andrei.engine.repository.interfaces.ChatRepository
import com.andrei.engine.repositoryInterfaces.ChatAPI
import com.andreia.carrental.requestModels.CreateMessageRequest
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
        private val userDataManager: UserDataManager,
        private val callRunner: CallRunner,
        private val chatAPI: ChatAPI,
        private val messageDao: MessageDao

): ChatRepository {


    override val messageToUnsendState:MutableSharedFlow<State<Message>>  = MutableSharedFlow(replay = 0,
            extraBufferCapacity = 1,
            onBufferOverflow = BufferOverflow.DROP_OLDEST )

    override val messageToSendState: MutableSharedFlow<State<Message>> =  MutableSharedFlow(replay = 0,
            extraBufferCapacity = 1,
            onBufferOverflow = BufferOverflow.DROP_OLDEST )


    override suspend fun getInitialChatMessages(chatID:Long):List<Message>{
       return  messageDao.findLastChatMessages(chatID)
    }


    override suspend fun sendMessage(text: String, currentChatID: Long){

        val requestModel = CreateMessageRequest(
                content = text,
                senderID = userDataManager.getUserID(),
                chatID = currentChatID
        )
         callRunner.makeApiCall { chatAPI.postMessage(requestModel) }.collect {
            messageToSendState.emit(it)
        }
    }

    override suspend fun unsendMessage(message: Message){
        callRunner.makeApiCall{ chatAPI.modifyMessage(message.messageID)}.collectLatest{ state->
            messageToUnsendState.emit(state)
        }
    }


    override suspend fun fetchUserChats(): Flow<State<List<ChatDTO>>> {
        val flow = callRunner.makeApiCall { chatAPI.getAllUserChats(userDataManager.getUserID()) }
        flow.collect {
            if (it is State.Success) {
                //insert into room
                if (it.data != null) {
                    messageDao.clean()
                    it.data.forEach { chatDTO ->
                        val messages = chatDTO.lastMessages.map { messageDTO ->
                            messageDTO.toMessage()
                        }
                        messageDao.insertMessages(messages)
                    }
                }
            }
        }
        return flow
    }

}