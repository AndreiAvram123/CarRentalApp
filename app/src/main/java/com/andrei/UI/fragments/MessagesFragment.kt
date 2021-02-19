package com.andrei.UI.fragments

import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.andrei.UI.adapters.messages.IncomingUnsendMessageViewHolder
import com.andrei.UI.adapters.messages.OutgoingMessageViewHolder
import com.andrei.UI.bottomSheets.OptionsMessageBottomSheet
import com.andrei.carrental.R
import com.andrei.carrental.UserDataManager
import com.andrei.carrental.databinding.FragmentMessagesBinding
import com.andrei.carrental.entities.Message
import com.andrei.carrental.entities.MessageType
import com.andrei.carrental.viewmodels.ViewModelChat
import com.andrei.services.MessengerService
import com.andrei.utils.loadFromURl
import com.andrei.utils.reObserve
import com.stfalcon.chatkit.commons.ImageLoader
import com.stfalcon.chatkit.messages.MessageHolders
import com.stfalcon.chatkit.messages.MessagesListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class MessagesFragment :BaseFragment(R.layout.fragment_messages) , MessageHolders.ContentChecker<Message> {

    private val binding:FragmentMessagesBinding by viewBinding ()
    private val viewModelChat:ViewModelChat by activityViewModels()
    private val navArgs:MessagesFragmentArgs by navArgs()
    private var skipScroll: Boolean = false

    @Inject
    lateinit var messengerService: MessengerService

    private val bottomSheet:OptionsMessageBottomSheet by lazy {
        OptionsMessageBottomSheet(this::unsendMessage, this::sheetClosed).apply {
            isCancelable = false
        }
    }

    private fun sheetClosed() {
          Handler(Looper.getMainLooper()).postDelayed({
              skipScroll = false
          }, 200)
    }

    private  val imageLoader = ImageLoader { imageView, url, _ -> url?.let { imageView.loadFromURl(it) } }


    private val messagesAdapter:MessagesListAdapter<Message> by lazy {
        MessagesListAdapter<Message>(UserDataManager.getUserID(requireContext()).toString(),
                getAdapterHolders(),imageLoader)
    }


    inner class CustomSelectionListener(private val currentUserID: String):MessagesListAdapter.SelectionListener{
        override fun onSelectionChanged(count: Int) {
            if (count > 0) {
                if (messagesAdapter.selectedMessages.first().user.id != currentUserID) {
                    messagesAdapter.unselectAllItems()
                } else {
                    showBottomSheet()
                }
            }
        }

    }

    private fun getAdapterHolders():MessageHolders{
        return MessageHolders()
                .registerContentType(
                        MessageType.MESSAGE_UNSENT.id.toByte(),
                        IncomingUnsendMessageViewHolder::class.java,
                        R.layout.item_custom_incoming_unsend_message,
                        OutgoingMessageViewHolder::class.java,
                        R.layout.item_custom_outcoming_unsend_message,
                        this@MessagesFragment)

    }


    private fun configureMessageAdapter() {
            messagesAdapter.apply {
                enableSelectionMode(CustomSelectionListener(UserDataManager.getUserID(requireContext()).toString()))
            }
    }

    private fun unsendMessage(){
        val messages =   messagesAdapter.selectedMessages.first()
        messagesAdapter.unselectAllItems()
        viewModelChat.unsendMessage(messages)
    }


    private fun showBottomSheet() {
        skipScroll = true
        bottomSheet.show(requireActivity().supportFragmentManager, "bottomSheetMessage")
    }



    override fun initializeUI() {
        viewModelChat.setCurrentOpenedChatID(navArgs.chatID)
        configureToolbar()
        configureRV()
        configureMessageAdapter()
        populateRVWithData()
        attachListeners()
        attachObservers()
    }

    private fun attachObservers() {
        messengerService.getUserOnlineObservable(navArgs.chatID).reObserve(viewLifecycleOwner) {
            if (it) {
                binding.toolbar.subtitle = "Online"
            } else {
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
        binding.messagesList.setAdapter(messagesAdapter)

        binding.messagesList.addOnLayoutChangeListener { _, _, _, bottom, _, _, _, _, oldBottom ->
            if (shouldScroll(bottom, oldBottom)) {
                binding.messagesList.postDelayed({
                    binding.messagesList.smoothScrollToPosition(0)
                }, 50)
            }
        }
    }

    private fun shouldScroll(bottom: Int, oldBottom: Int):Boolean{
        return bottom < oldBottom && messagesAdapter.selectedMessages.isEmpty() && !skipScroll
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

    private fun addAdapterData(data: List<Message>){
        lifecycleScope.launch(Dispatchers.Main) {
            data.forEach {
                messagesAdapter.addToStart(it, true)
            }
            observeNewMessages()
            observerUnsentMessages()
        }
    }

    private fun observerUnsentMessages() {
        messengerService.getObservableUnsentMessage(navArgs.chatID).reObserve(viewLifecycleOwner){
            if(it!=null){
              messagesAdapter.update(it)
            }
        }
    }

    private fun observeNewMessages() {
        messengerService.getObservableLastMessage(navArgs.chatID).reObserve(viewLifecycleOwner){
            if(it != null){
                messagesAdapter.addToStart(it,true)
            }
        }
    }

    override fun hasContentFor(message: Message, type: Byte): Boolean {
         if(type == message.messageType.id.toByte()){
             return true
         }
        return false
    }
}


