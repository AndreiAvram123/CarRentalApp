package com.andrei.UI.fragments

import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.andrei.UI.adapters.CustomDivider
import com.andrei.carrental.R
import com.andrei.carrental.databinding.FragmentMessagesBinding
import com.andrei.carrental.entities.Image
import com.andrei.carrental.entities.Message
import com.andrei.carrental.viewmodels.ViewModelChat
import com.andrei.engine.helpers.UserManager
import com.andrei.utils.loadFromURl
import com.andrei.utils.observeOnce
import com.andrei.utils.reObserve
import com.stfalcon.chatkit.commons.ImageLoader
import com.stfalcon.chatkit.messages.MessageInput
import com.stfalcon.chatkit.messages.MessagesListAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.Dispatcher
import javax.inject.Inject

class MessagesFragment :BaseFragment(R.layout.fragment_messages) {

    private val binding:FragmentMessagesBinding by viewBinding ()
    private val viewModelChat:ViewModelChat by activityViewModels()
    private val navArgs:MessagesFragmentArgs by navArgs()

     private  val imageLoader = ImageLoader { imageView, url, _ -> url?.let { imageView.loadFromURl(it) } }

    private val messagesAdapter:MessagesListAdapter<Message> by lazy {
        MessagesListAdapter(UserManager.getUserID(requireContext()).toString(),imageLoader)
    }


    override fun initializeUI() {
        viewModelChat.setCurrentOpenedChatID(navArgs.chatID)
        populateRVWithData()
        attachListeners()
    }


    private fun attachListeners() {
        binding.inputMessage.setInputListener {
            viewModelChat.sendMessage(it.toString())
            true
        }
    }

    private fun populateRVWithData(){
        lifecycleScope.launch(Dispatchers.IO) {
            val data = viewModelChat.getInitialChatMessages()
            addAdapterData(data)
        }
    }

    private fun addAdapterData(data:List<Message>){
        lifecycleScope.launch(Dispatchers.Main) {
            data.forEach {
                messagesAdapter.addToStart(it,true)
            }
            binding.messagesList.setAdapter(messagesAdapter)
            startObservingNewMessage()
        }
    }

    private fun startObservingNewMessage() {
        viewModelChat.lastChatMessage.reObserve(viewLifecycleOwner){
            messagesAdapter.addToStart(it,true)
        }
    }
}


