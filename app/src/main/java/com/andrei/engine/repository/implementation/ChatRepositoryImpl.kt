package com.andrei.engine.repository.implementation

import com.andrei.carrental.entities.Message
import com.andrei.carrental.room.dao.MessageDao
import com.andrei.engine.CallRunner
import com.andrei.engine.DTOEntities.ChatDTO
import com.andrei.engine.DTOEntities.toMessage
import com.andrei.engine.State
import com.andrei.engine.repository.interfaces.ChatRepository
import com.andrei.engine.APIs.ChatAPI
import com.andreia.carrental.requestModels.CreateMessageRequest
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
        private val callRunner: CallRunner,
        private val chatAPI: ChatAPI,
        private val messageDao: MessageDao

): ChatRepository {


    override val messageToUnsendState: MutableSharedFlow<State<Message>> = MutableSharedFlow(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    override val messageToSendState: MutableStateFlow<State<Message>> =
        MutableStateFlow(State.Default)

    override suspend fun getInitialChatMessages(chatID: Long): List<Message> {
        return messageDao.findLastChatMessages(chatID)
    }


    override suspend fun sendMessage(createMessageRequest: CreateMessageRequest) {

        callRunner.makeApiCall { chatAPI.postMessage(createMessageRequest) }.collect {
            messageToSendState.emit(it)
        }
    }

    override suspend fun unsendMessage(message: Message) {
        callRunner.makeApiCall { chatAPI.modifyMessage(message.messageID) }.collectLatest { state ->
            messageToUnsendState.emit(state)
        }
    }


    override suspend fun fetchUserChats(userID: Long): Flow<State<List<ChatDTO>>> {
        val flow = callRunner.makeApiCall { chatAPI.getAllUserChats(userID) }
        flow.collect {
            if (it is State.Success) {
                //insert into room
                messageDao.clean()
                it.data.forEach { chatDTO ->
                    val messages = chatDTO.lastMessages.map { messageDTO ->
                        messageDTO.toMessage()
                    }
                    messageDao.insertMessages(messages)
                }
            }
        }
        return flow
    }

    override fun loadMoreMessages(chatID: Long, offset: Int): Flow<State<List<Message>>> {
        return callRunner.makeApiCall {
            chatAPI.loadMoreMessages(chatID, offset)
        }.transform {
            when {
                it is State.Success -> {
                    val mappedData = it.data.map { messageDTO -> messageDTO.toMessage() }
                    emit(State.Success(mappedData))
                }
                it is State.Loading -> emit(State.Loading)
                it is State.Error -> emit(State.Error(it.error))
            }
        }

    }
}