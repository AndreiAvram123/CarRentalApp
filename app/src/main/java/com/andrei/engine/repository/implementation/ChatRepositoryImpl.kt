package com.andrei.engine.repository.implementation

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.liveData
import com.andrei.carrental.entities.Message
import com.andrei.carrental.helpers.ConsumeLiveData
import com.andrei.carrental.room.dao.MessageDao
import com.andrei.engine.CallRunner
import com.andrei.engine.DTOEntities.ChatDTO
import com.andrei.engine.DTOEntities.toMessage
import com.andrei.engine.State
import com.andrei.engine.helpers.UserManager
import com.andrei.engine.repository.interfaces.ChatRepository
import com.andrei.engine.repositoryInterfaces.ChatAPI
import com.andreia.carrental.requestModels.CreateMessageRequest
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
        private val userManager: UserManager,
        private val callRunner: CallRunner,
        private val chatAPI: ChatAPI,
        private val messageDao: MessageDao

): ChatRepository {

    override val userChats: LiveData<State<List<ChatDTO>>> = Transformations.switchMap(userManager.userLoginData) {
        fetchUserChats(it.id)
    }

    override val messageToUnsendState:ConsumeLiveData<State<Message>> by lazy {
        ConsumeLiveData()
    }

    override suspend fun getInitialChatMessages(chatID:Long):List<Message>{
       return  messageDao.findLastChatMessages(chatID)
    }


    override suspend fun sendMessage(text: String, currentChatID: Long) {
        val userID =  userManager.userLoginData.value?.id
        check(userID!=null){}
        val requestModel = CreateMessageRequest(
            content = text,
            senderID = userID,
            chatID = currentChatID
        )
        callRunner.makeApiCall(chatAPI.postMessage(requestModel)){
          print(it)
        }
    }

    override suspend fun unsendMessage(message: Message) {
        callRunner.makeApiCall(chatAPI.modifyMessage(message.messageID)){
            messageToUnsendState.postValue(it)
        }
    }



    private fun fetchUserChats(userID: Long): LiveData<State<List<ChatDTO>>> = liveData {
        callRunner.makeApiCall(chatAPI.getAllUserChats(userID)) {
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
            emit(it)
        }
    }

}