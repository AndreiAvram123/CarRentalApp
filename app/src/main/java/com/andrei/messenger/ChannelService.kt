package com.andrei.messenger

import androidx.lifecycle.LiveData
import com.andrei.carrental.entities.Message
import com.andrei.carrental.entities.User
import com.andrei.carrental.factories.PusherFactory

import com.andrei.carrental.room.dao.MessageDao
import com.andrei.engine.DTOEntities.MessageDTO
import com.google.gson.Gson
import com.pusher.client.Pusher
import com.pusher.client.PusherOptions
import com.pusher.client.channel.Channel
import com.pusher.client.channel.SubscriptionEventListener
import com.pusher.client.connection.ConnectionState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber


class ChannelService(
     val chatID:Long,
     private val pusherConfig: PusherConfig,
     private val messageDao: MessageDao,
     private val coroutineScope: CoroutineScope
    ) {

    private val pusherPresenceChannel = PusherFactory.createPusher(pusherConfig)
    private val pusherChannel = PusherFactory.createPusher(pusherConfig)

    private val _isUserOnline:MutableStateFlow<Boolean> =  MutableStateFlow(false)


    val isUserOnline:StateFlow<Boolean>
    get() = _isUserOnline.asStateFlow()




    private val eventNewMessageListener = SubscriptionEventListener{
        val messageDTO = Gson().fromJson(it.data, MessageDTO::class.java)
        coroutineScope.launch{
            val message = messageDTO.toMessage()
            messageDao.insertMessage(message)
        }
    }

    private val eventDeleteMessageListener = SubscriptionEventListener {
        val messageDTO = Gson().fromJson(it.data, MessageDTO::class.java)
        coroutineScope.launch {
            val message = messageDTO.toMessage()
             messageDao.updateMessage(message)
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
            coroutineScope.launch { _isUserOnline.emit(true) }
        }
        listener.onUserUnsubscribed = {_,_ ->
            coroutineScope.launch { _isUserOnline.emit(false) }
        }
        listener.onUsersInformationRetrieved = { _,users ->
            coroutineScope.launch {
                _isUserOnline.emit(users != null && users.size > 1)
            }
        }

    }


    fun connect(){
        if(pusherChannel.isDisconnected()){
            pusherChannel.connect()
        }
        if(pusherPresenceChannel.isDisconnected()){
            pusherPresenceChannel.connect()
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