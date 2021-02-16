package com.andrei.services

import android.util.Log
import com.pusher.client.channel.PresenceChannelEventListener
import com.pusher.client.channel.PusherEvent
import com.pusher.client.channel.User
import java.lang.Exception

class CustomPresenceChannelListener : PresenceChannelEventListener {

    var onEvent: ((event:PusherEvent?) -> Unit)? = null
    var onUserSubscribed: ((channelName:String?,user:User?)-> Unit)? = null
    var onUserUnsubscribed: ((channelName:String?,user:User?)-> Unit)? = null
    var onUsersInformationRetrieved:((channelName: String?, users: MutableSet<User>?) -> Unit)? = null

    override fun onEvent(event: PusherEvent?) {
        onEvent?.invoke(event)
    }

    override fun onSubscriptionSucceeded(channelName: String?) {
    }

    override fun onAuthenticationFailure(message: String?, e: Exception?) {
    }

    override fun onUsersInformationReceived(channelName: String?, users: MutableSet<User>?) {
        onUsersInformationRetrieved?.invoke(channelName,users)
    }

    override fun userSubscribed(channelName: String?, user: User?) {
       onUserSubscribed?.invoke(channelName,user)
    }

    override fun userUnsubscribed(channelName: String?, user: User?) {
        onUserUnsubscribed?.invoke(channelName,user)
    }
}