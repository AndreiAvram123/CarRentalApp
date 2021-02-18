package com.andrei.UI.fragments

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.andrei.carrental.R
import com.andrei.carrental.databinding.FragmentMessagesBinding
import com.andrei.carrental.entities.Message
import com.andrei.carrental.viewmodels.ViewModelChat
import com.andrei.engine.helpers.UserManager
import com.andrei.services.MessengerService
import com.andrei.utils.loadFromURl
import com.andrei.utils.reObserve
import com.stfalcon.chatkit.commons.ImageLoader
import com.stfalcon.chatkit.messages.MessagesListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class MessagesFragment :BaseFragment(R.layout.fragment_messages) {

    private val binding:FragmentMessagesBinding by viewBinding ()
    private val viewModelChat:ViewModelChat by activityViewModels()
    private val navArgs:MessagesFragmentArgs by navArgs()

    @Inject
    lateinit var messengerService: MessengerService


     private  val imageLoader = ImageLoader { imageView, url, _ -> url?.let { imageView.loadFromURl(it) } }

    private val messagesAdapter:MessagesListAdapter<Message> by lazy {
        MessagesListAdapter(UserManager.getUserID(requireContext()).toString(),imageLoader)
    }


    override fun initializeUI() {
        viewModelChat.setCurrentOpenedChatID(navArgs.chatID)
        configureToolbar()
        configureRV()
        populateRVWithData()
        attachListeners()
        attachObservers()

    }

    private fun attachObservers() {
         messengerService.getUserOnlineObservable(navArgs.chatID).reObserve(viewLifecycleOwner){
             if(it){
                 binding.toolbar.subtitle = "Online"
             }else{
                 binding.toolbar.subtitle = "Offline"
             }

         }
    }

    private fun configureToolbar() {
            val activity =  (requireActivity() as AppCompatActivity)
            activity.apply {
                setSupportActionBar(binding.toolbar)
                supportActionBar?.setDisplayHomeAsUpEnabled(true)
                supportActionBar?.setDisplayShowHomeEnabled(true)
                messengerService.getChatFriend(navArgs.chatID)?.let { supportActionBar?.title = it.username }
            }

        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }



    private fun configureRV() {
        binding.messagesList.addOnLayoutChangeListener { _, _, _, bottom, _, _, _, _, oldBottom ->
            if (bottom < oldBottom) {
                binding.messagesList.postDelayed({
                    binding.messagesList.smoothScrollToPosition(0)
                }, 100)
            }
        }
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
        messengerService.getObservableLastMessage(navArgs.chatID).reObserve(viewLifecycleOwner){
            if(it != null){
                messagesAdapter.addToStart(it,true)
            }
        }
    }
}


