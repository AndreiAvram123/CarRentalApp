package com.andrei.UI.adapters.messages

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.andrei.carrental.databinding.MessageImageReceivedBinding
import com.andrei.carrental.databinding.MessageImageSentBinding
import com.andrei.carrental.databinding.MessageReceivedBinding
import com.andrei.carrental.databinding.MessageSentBinding
import com.andrei.carrental.entities.Message


class MessageAdapter(val expand: (imageURL: String) -> Unit) : RecyclerView.Adapter<MessageAdapter.ViewHolderMessage>() {
    private val messages: ArrayList<Message> = ArrayList()

    private var adapterRecyclerView: RecyclerView? = null




    inner class MessageReceivedViewHolderMessage(val binding: MessageReceivedBinding) : ViewHolderMessage(binding.root) {
        override fun bind(message: Message) {
            binding.message = message
        }
    }

    inner class MessageSentViewHolderMessage(val binding: MessageSentBinding) : ViewHolderMessage(binding.root) {
        override fun bind(message: Message) {
            binding.message = message
        }
    }
    inner class MessageImageSentViewHolderMessage(val binding: MessageImageSentBinding) : ViewHolderMessage(binding.root) {
        override fun bind(message: Message) {
            binding.message = message
            binding.messageImage.setOnClickListener {
                expandImage(message)
            }
        }
    }

    inner class MessageImageReceivedViewHolderMessage(val binding: MessageImageReceivedBinding) : ViewHolderMessage(binding.root) {
        override fun bind(message: Message) {
            binding.message = message
            binding.messageImage.setOnClickListener {
                expandImage(message)
            }
        }
    }

    abstract inner class ViewHolderMessage(view: View) : RecyclerView.ViewHolder(view) {
        abstract fun bind(message: Message)
    }

    fun expandImage(message: Message) {
        expand(message.content)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageAdapter.ViewHolderMessage {
        val inflator: LayoutInflater = LayoutInflater.from(parent.context)
                val binding = MessageReceivedBinding.inflate(inflator, parent, false)
                return MessageReceivedViewHolderMessage(binding)

    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        adapterRecyclerView = recyclerView

    }

    override fun getItemCount(): Int {
        return messages.size
    }

    override fun onBindViewHolder(viewHolderMessage: MessageAdapter.ViewHolderMessage, position: Int) = viewHolderMessage.bind(messages[position])


    fun addMessage(message: Message) {
        if(messages.find { it.id == message.id } == null) {
            messages.add(message)
            notifyItemInserted(messages.size - 1)
            scrollToLast()
        }
    }

    private fun scrollToLast() {
        adapterRecyclerView?.scrollToPosition(messages.size - 1)
    }


    fun setData(newMessages: List<Message>) {
        newMessages.forEach {newMessage->
             if(messages.find { it.id ==  newMessage.id} == null){
                 addMessage(newMessage)
             }
        }
    }

    fun clear() {
        messages.clear()
    }


}