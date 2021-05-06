package com.andrei.messenger

import com.pusher.client.PusherOptions

data class PusherConfig(
         val pusherOptions: PusherOptions,
         val pusherKey:String,
)