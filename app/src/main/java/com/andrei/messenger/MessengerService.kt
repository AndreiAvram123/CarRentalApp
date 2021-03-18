package com.andrei.messenger

import android.content.Context
import com.andrei.DI.annotations.DefaultGlobalScope
import com.andrei.carrental.R
import com.andrei.carrental.entities.Message
import com.andrei.carrental.entities.ObservableChat
import com.andrei.carrental.entities.User
import com.andrei.carrental.room.dao.MessageDao
import com.andrei.engine.DTOEntities.ChatDTO
import com.andrei.engine.DTOEntities.toUser
import com.andrei.utils.defaultSharedFlow
import com.pusher.client.PusherOptions
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@ActivityScoped
class MessengerService @Inject constructor (
         private val pusherConfig: PusherConfig,
        private val messagesDao: MessageDao,
        @DefaultGlobalScope private val coroutineScope: CoroutineScope
        ){


    private val channels : MutableMap<Long,ChannelService> = mutableMapOf()

    fun configureChannels(chats:List<ChatDTO>){
          channels.forEach { it.value.disconnect() }
          channels.clear()
          channels.putAll(chats.map { it.id to ChannelService(it.id,
                   pusherConfig,
                   messagesDao,
                   it.friend.toUser(),
                   coroutineScope
          )
               }.toMap())
    }

    fun getObservableChats():List<ObservableChat> = channels.map {
            ObservableChat(
                    id = it.value.chatID,
                    friend = it.value.friend,
                    isUserOnline = it.value.isUserOnline,
                    lastMessage = messagesDao.findLastChatMessage(it.value.chatID)
            )
        }

     fun getLastMessageFlow(chatID:Long): SharedFlow<Message> =
             messagesDao.findLastChatMessageDistinct(chatID).defaultSharedFlow(coroutineScope)

     fun getUnsentMessageFlow(chatID:Long):SharedFlow<Message> =
             messagesDao.findLastUnsentChatMessageDistinct(chatID).defaultSharedFlow(coroutineScope)


    fun getChatFriend(chatID: Long):User?{
        val channelService = channels[chatID]
        channelService?.let { return it.friend }
        return null
    }
    fun getUserOnlineFlow(chatID: Long):StateFlow<Boolean>{
        channels[chatID]?.let { return it.isUserOnline }
        return MutableStateFlow(false).asStateFlow()
    }


    fun connect(){
      channels.forEach{
          it.value.connect()
      }
    }
    fun disconnect(){
        channels.forEach{
            it.value.disconnect()
        }
    }
}