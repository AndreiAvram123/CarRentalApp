package com.andrei.services

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.andrei.carrental.R
import com.andrei.carrental.entities.Message
import com.andrei.carrental.entities.MessageType
import com.andrei.carrental.entities.User
import com.andrei.carrental.factories.PusherFactory
import com.andrei.carrental.room.dao.MessageDao
import com.andrei.engine.DTOEntities.MessageDTO
import com.andrei.engine.DTOEntities.UserDTO
import com.andrei.engine.DTOEntities.toMessage
import com.google.gson.Gson
import com.pusher.client.Pusher
import com.pusher.client.PusherOptions
import com.pusher.client.channel.PusherEvent
import com.pusher.client.channel.SubscriptionEventListener


class ChannelService(
    private val chatID:Long,
    private val pusherOptions: PusherOptions,
    private val pusherKey:String,
    private val messageDao: MessageDao,
    val friend:User,
    ) {

    private val pushers :MutableList<Pusher> = mutableListOf()

    private val _isUserOnline:MutableLiveData<Boolean> by lazy {
        MutableLiveData(false)
    }

    val isUserOnline:LiveData<Boolean>
    get() = _isUserOnline


    val lastMessageDTO:LiveData<Message> by lazy {
        messageDao.findLastChatMessage(chatID)
    }

    private val eventNewMessageListener = SubscriptionEventListener{
        val messageDTO = Gson().fromJson(it.data, MessageDTO::class.java)
        when {
            messageDTO.isImageMessage && messageDTO.sender.id != friend.id -> messageDao.insertMessage(messageDTO.toMessage(MessageType.MESSAGE_SENT_IMAGE))
            messageDTO.isImageMessage && messageDTO.sender.id == friend.id -> messageDao.insertMessage(messageDTO.toMessage(MessageType.MESSAGE_RECEIVED_IMAGE))

            !messageDTO.isImageMessage && messageDTO.sender.id != friend.id -> messageDao.insertMessage(messageDTO.toMessage(MessageType.MESSAGE_SENT_TEXT))
            !messageDTO.isImageMessage && messageDTO.sender.id == friend.id -> messageDao.insertMessage(messageDTO.toMessage(MessageType.MESSAGE_RECEIVED_TEXT))
        }
    }


    init {
        bindToNewUserAddedEvent()
        bindToNewMessagesEvent()
    }

    private fun bindToNewMessagesEvent() {
        val pusher = PusherFactory.createPusher(pusherOptions,pusherKey)
        pushers.add(pusher)
        val messagesChannel = pusher.subscribe("chats-$chatID")
         messagesChannel.bind(eventNewMessage,eventNewMessageListener)
    }

    private fun bindToNewUserAddedEvent() {
        val listener = CustomPresenceChannelListener()
        val pusher = PusherFactory.createPusher(pusherOptions,pusherKey)
        pushers.add(pusher)
        pusher.subscribePresence("presence-chats-$chatID",listener, eventNewUserAdded)

        listener.onUserSubscribed = { _,_ ->
            _isUserOnline.postValue(true)
        }
        listener.onUserUnsubscribed = {_,_ ->
           _isUserOnline.postValue(false)
        }
        listener.onUsersInformationRetrieved = { _,users ->
            _isUserOnline.postValue(users != null && users.size > 1)
        }

    }


    fun connect(){
        pushers.forEach {
            it.connect()
        }
    }

    companion object {
        private const val eventNewUserAdded = "pusher:member_added"
        private const val eventNewMessage = "new_message"
    }

}