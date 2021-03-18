package com.andrei.UI.adapters.chats

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.andrei.UI.adapters.bookings.ViewHolderChat
import com.andrei.carrental.databinding.ItemChatBinding
import com.andrei.carrental.entities.ObservableChat
import com.andrei.utils.reObserve
import kotlinx.coroutines.flow.collect


class ChatsAdapter(private val _lifecycleOwner: LifecycleOwner,
                   private val navigateToMessagesCallback: (chatID:Long)->Unit) : RecyclerView.Adapter<ViewHolderChat>() {

    
    private var observableChats:MutableList<ObservableChat> = mutableListOf()

    fun setData(data: List<ObservableChat>) {
        observableChats.clear()
        observableChats.addAll(data)
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
        holder.bind(observableChats[position])
        holder.setIsRecyclable(false)
    }

    override fun getItemCount(): Int  = observableChats.size



    inner class ViewHolderChatImpl(private val binding: ItemChatBinding) : ViewHolderChat(binding.root) {
        override fun bind(observableChat: ObservableChat) {
           binding.chat = observableChat
            _lifecycleOwner.lifecycleScope.launchWhenResumed {
                observableChat.isUserOnline.collect {
                    binding.isUserOnline = it
                }
            }
            binding.root.setOnClickListener {
                navigateToMessagesCallback(observableChat.id)
            }
        }


    }


}