package com.andrei.services

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.andrei.carrental.entities.Message
import com.andrei.carrental.entities.User
import com.andrei.carrental.factories.PusherFactory
import com.andrei.carrental.room.dao.MessageDao
import com.andrei.engine.DTOEntities.MessageDTO
import com.andrei.engine.DTOEntities.toMessage
import com.google.gson.Gson
import com.pusher.client.Pusher
import com.pusher.client.PusherOptions
import com.pusher.client.channel.Channel
import com.pusher.client.channel.SubscriptionEventListener
import com.pusher.client.connection.ConnectionState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class ChannelService(
     val chatID:Long,
    private val pusherOptions: PusherOptions,
    private val pusherKey:String,
    private val messageDao: MessageDao,
    val friend:User,
    ) {

    private val pusherPresenceChannel = PusherFactory.createPusher(pusherOptions,pusherKey)
    private val pusherChannel = PusherFactory.createPusher(pusherOptions,pusherKey)

    private val _isUserOnline:MutableLiveData<Boolean> by lazy {
        MutableLiveData(false)
    }

    val isUserOnline:LiveData<Boolean>
    get() = _isUserOnline


    val lastChatMessage:LiveData<Message> by lazy {
        messageDao.findLastChatMessage(chatID)
    }

    private val eventNewMessageListener = SubscriptionEventListener{
        val messageDTO = Gson().fromJson(it.data, MessageDTO::class.java)
        GlobalScope.launch (Dispatchers.IO){
            messageDao.insertMessage(messageDTO.toMessage())
        }
    }

    private val eventDeleteMessageListener = SubscriptionEventListener {
        val messageDTO = Gson().fromJson(it.data, MessageDTO::class.java)
        GlobalScope.launch (Dispatchers.IO){
            messageDao.deleteMessage(messageDTO.toMessage())
        }
    }


    init {
        subscribeToPresenceChannel()
        subscribeToChannel()
    }

    private fun bindToDeleteMessageEvent(channel: Channel) {
         channel.bind(eventDeletedMessage,eventDeleteMessageListener)
    }

    private fun subscribeToChannel() {
        val messagesChannel = pusherChannel.subscribe("chats-$chatID")
        bindToNewMessageEvent(messagesChannel)
        bindToDeleteMessageEvent(messagesChannel)
    }

    private fun bindToNewMessageEvent(channel: Channel){
        channel.bind(eventNewMessage,eventNewMessageListener)
    }


    private fun subscribeToPresenceChannel() {
        val listener = CustomPresenceChannelListener()

        pusherPresenceChannel.subscribePresence("presence-chats-$chatID",listener, eventNewUserAdded)

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
        if(pusherChannel.isDisconnected()){
            pusherChannel.connect()
        }
        if(pusherPresenceChannel.isDisconnected()){
            pusherChannel.connect()
        }
    }

    fun disconnect(){
        if(pusherChannel.isConnected()){
            pusherChannel.disconnect()
        }
        if(pusherPresenceChannel.isConnected()){
            pusherPresenceChannel.disconnect()
        }
    }

    private fun Pusher.isDisconnected():Boolean{
        return connection.state == ConnectionState.DISCONNECTED || connection.state == ConnectionState.DISCONNECTING
    }
    private fun Pusher.isConnected():Boolean{
        return connection.state == ConnectionState.CONNECTED ||  connection.state == ConnectionState.CONNECTING || connection.state == ConnectionState.RECONNECTING
    }

    companion object {
        private const val eventNewUserAdded = "pusher:member_added"
        private const val eventNewMessage = "new_message"
        private const val eventDeletedMessage = "message_deleted"
    }

}