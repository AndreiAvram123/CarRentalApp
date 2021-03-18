package com.andrei.UI.fragments

import android.content.Intent
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
import com.andrei.messenger.MessengerService
import com.andrei.utils.loadFromURl
import com.andrei.utils.reObserve
import com.andreia.carrental.requestModels.CreateMessageRequest
import com.stfalcon.chatkit.commons.ImageLoader
import com.stfalcon.chatkit.messages.MessageHolders
import com.stfalcon.chatkit.messages.MessagesListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterNotNull
import pl.aprilapps.easyphotopicker.DefaultCallback
import pl.aprilapps.easyphotopicker.EasyImage
import pl.aprilapps.easyphotopicker.MediaFile
import pl.aprilapps.easyphotopicker.MediaSource
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class MessagesFragment :BaseFragment(R.layout.fragment_messages) ,
    MessageHolders.ContentChecker<Message> {

    private val binding:FragmentMessagesBinding by viewBinding ()
    private val viewModelChat:ViewModelChat by activityViewModels()

    private val navArgs:MessagesFragmentArgs by navArgs()
    private var skipScroll: Boolean = false

    @Inject
    lateinit var easyImage: EasyImage

    @Inject
    lateinit var messengerService: MessengerService

    @Inject
    lateinit var userDataManager: UserDataManager


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


    private val messagesAdapter:CustomMessagesListAdapter by lazy {
        CustomMessagesListAdapter(userDataManager.getUserID().toString(), getAdapterHolders(),imageLoader).apply {
            enableSelectionMode(CustomSelectionListener(userDataManager.getUserID().toString()))
        }
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
         easyImage.handleActivityResult(requestCode,resultCode,data,requireActivity(), object : DefaultCallback() {
             override fun onMediaFilesPicked(imageFiles: Array<MediaFile>, source: MediaSource) {
                 viewModelChat.sendImages(imageFiles.asList(),navArgs.chatID,userDataManager.getUserID())
             }
         })
    }


    private fun getAdapterHolders():MessageHolders{
        val messageHolders = MessageHolders()
        messageHolders.registerContentType(
                MessageType.MESSAGE_UNSENT.id.toByte(),
                IncomingUnsendMessageViewHolder::class.java,
                R.layout.item_custom_incoming_unsend_message,
                OutgoingMessageViewHolder::class.java,
                R.layout.item_custom_outcoming_unsend_message,
                this@MessagesFragment)

        return messageHolders
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
        populateRVWithData()
        attachListeners()
        attachObservers()
    }

    private fun attachObservers() {
        lifecycleScope.launchWhenResumed {
            messengerService.getUserOnlineFlow(navArgs.chatID).collect {
                binding.toolbar.subtitle = if(it) getString(R.string.online) else getString(R.string.offline)
            }
        }
        lifecycleScope.launchWhenResumed {
            messengerService.getUnsentMessageFlow(navArgs.chatID).collect {
                messagesAdapter.update(it)
            }
        }


        lifecycleScope.launchWhenResumed {
            messengerService.getLastMessageFlow(navArgs.chatID).collect {
                messagesAdapter.tryAddMessageToStart(it)
            }
        }
    }

    private fun configureToolbar() {
             (requireActivity() as AppCompatActivity).apply {
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



    private fun sendTextMessage(messageText:String){
        val createMessageModel = CreateMessageRequest(
                senderID = userDataManager.getUserID(),
                chatID = navArgs.chatID,
                messageType = MessageType.MESSAGE_TEXT,
                textContent = messageText
        )
        viewModelChat.sendMessage(createMessageModel)
    }



    private fun attachListeners() {
        binding.inputMessage.setInputListener {
            sendTextMessage(it.toString())
            true
        }
        binding.inputMessage.setAttachmentsListener {
            easyImage.openChooser(this)
        }
    }

    private fun populateRVWithData(){
        lifecycleScope.launchWhenResumed {
            viewModelChat.getChatMessages()
            viewModelChat.currentOpenedChatMessages.collect {
                addDataToAdapter(it.reversed())
            }
        }
    }

    private fun addDataToAdapter(data: List<Message>){
            data.forEach {
                messagesAdapter.tryAddMessageToStart(it)
            }

    }

    override fun onDestroy() {
        super.onDestroy()
        viewModelChat.resetOpenedChat()
    }

    inner class CustomMessagesListAdapter(senderId:String, holders:MessageHolders,
                                          imageLoader: ImageLoader): MessagesListAdapter<Message>(senderId,holders,imageLoader){

      fun tryAddMessageToStart(message:Message){
          val containsItem = items.find {
              val item = it.item
              item is Message && item.id == message.id
          } != null

          if(!containsItem){
              addToStart(message,true)
          }
      }


                                                   }


    override fun hasContentFor(message: Message, type: Byte): Boolean =  type == message.messageType.id.toByte()

}


