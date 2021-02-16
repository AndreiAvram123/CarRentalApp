package com.andrei.services

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.andrei.carrental.R
import com.andrei.carrental.entities.chats.MessageEventWrapper
import com.andrei.carrental.factories.PusherFactory
import com.andreia.carrental.entities.Message
import com.google.gson.Gson
import com.pusher.client.Pusher
import com.pusher.client.PusherOptions
import com.pusher.client.channel.ChannelEventListener
import com.pusher.client.channel.PusherEvent

class ChannelService(
    private val chatID:Long,
    private val pusherOptions: PusherOptions,
    private val context:Context) {

    private val pushers :MutableList<Pusher> = mutableListOf()

    private val _isUserOnline:MutableLiveData<Boolean> by lazy {
        MutableLiveData(false)
    }

    val isUserOnline:LiveData<Boolean>
    get() = _isUserOnline

    private val _lastMessageSent:MutableLiveData<Message> by lazy {
        MutableLiveData()
    }
    val lastMessageSent:LiveData<Message>
    get() = _lastMessageSent



    init {
        bindToNewUserAddedEvent()
        bindToNewMessagesEvent()
    }

    private fun bindToNewMessagesEvent() {
        val pusher = PusherFactory.createPusher(pusherOptions,context.getString(R.string.pusher_key))
        pushers.add(pusher)
        val messagesChannel = pusher.subscribe("chats-$chatID")
        messagesChannel.bind(eventNewMessage){
          val message = Gson().fromJson(it.data,Message::class.java)
            _lastMessageSent.postValue(message)
        }
    }

    private fun bindToNewUserAddedEvent() {
        val listener = CustomPresenceChannelListener()
        val pusher = PusherFactory.createPusher(pusherOptions,context.getString(R.string.pusher_key))
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