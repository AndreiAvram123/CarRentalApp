package com.andrei.UI.adapters.chats

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.andrei.UI.adapters.bookings.ViewHolderChat
import com.andrei.carrental.databinding.ItemChatBinding
import com.andrei.carrental.entities.Chat
import com.andrei.utils.reObserve


class ChatsAdapter(private val _lifecycleOwner: LifecycleOwner) : RecyclerView.Adapter<ViewHolderChat>() {
    private var chats:MutableList<Chat> = mutableListOf()

    fun setData(data: List<Chat>) {
        chats.clear()
        chats.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderChat {
        val binding: ItemChatBinding = ItemChatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolderChatImpl(binding)
    }

    override fun onBindViewHolder(
        holder: ViewHolderChat,
        position: Int
    ) {
        holder.bind(chats[position])
        holder.setIsRecyclable(false)
    }

    override fun getItemCount(): Int {
        return chats.size
    }


    inner class ViewHolderChatImpl(private val binding: ItemChatBinding) : ViewHolderChat(binding.root) {
        override fun bind(chat: Chat) {
           binding.chat = chat
            chat.isUserOnline.reObserve(_lifecycleOwner){
              binding.isUserOnline = it
            }
        }


    }


}