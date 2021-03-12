package com.andrei.services

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.andrei.carrental.R
import com.andrei.carrental.entities.Message
import com.andrei.carrental.entities.ObservableChat
import com.andrei.carrental.entities.User
import com.andrei.carrental.room.dao.MessageDao
import com.andrei.engine.DTOEntities.ChatDTO
import com.andrei.engine.DTOEntities.toUser
import com.pusher.client.PusherOptions
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MessengerService @Inject constructor (
        @ApplicationContext private val context: Context,
        private val pusherOptions: PusherOptions,
        private val messagesDao: MessageDao
        ){


    private val channels : MutableMap<Long,ChannelService> = mutableMapOf()


    fun configureChannels(chats:List<ChatDTO>){
          channels.forEach { it.value.disconnect() }
          channels.clear()
          channels.putAll(chats.map { it.id to ChannelService(it.id,
                   pusherOptions,
                   context.getString(R.string.pusher_key),
                   messagesDao,
                   it.friend.toUser()
          )
               }.toMap())
    }

    fun getObservableChats():List<ObservableChat>{
        val chats = mutableListOf<ObservableChat>()
        channels.forEach {
            val chat = ObservableChat(
                    id = it.value.chatID,
                    friend = it.value.friend,
                    isUserOnline = it.value.isUserOnline,
                    lastMessage = messagesDao.findLastChatMessage(it.value.chatID)
            )
            chats.add(chat)
        }
        return chats
    }

    fun getFlowLastMessage(chatID:Long): Flow<Message> = messagesDao.findLastChatMessage(chatID)

    fun getChatFriend(chatID: Long):User?{
        val channelService = channels[chatID]
        channelService?.let { return it.friend }
        return null
    }
    fun getUserOnlineObservable(chatID: Long):LiveData<Boolean>{
        channels[chatID]?.let { return it.isUserOnline }
        return MutableLiveData()
    }

    fun getObservableUnsentMessage(chatID:Long):LiveData<Message>{
        channels[chatID]?.let { return it.unsentMessage }
        return MutableLiveData()
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