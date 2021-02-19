package com.andrei.UI.adapters.messages

import android.view.View
import android.widget.TextView
import com.andrei.carrental.R
import com.andrei.carrental.entities.Message
import com.stfalcon.chatkit.messages.MessageHolders.IncomingTextMessageViewHolder
import com.stfalcon.chatkit.utils.DateFormatter

class IncomingUnsendMessageViewHolder(itemView: View, payload: Any? = null) : IncomingTextMessageViewHolder<Message>(itemView, payload) {

    private val tvText: TextView = itemView.findViewById(R.id.messageText)

    override fun onBind(message: Message) {
        super.onBind(message)
        tvText.text = "Message unsent"
    }

}