package com.andrei.UI.fragments

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import coil.load
import com.andrei.UI.adapters.messages.IncomingUnsendMessageViewHolder
import com.andrei.UI.adapters.messages.OutgoingMessageViewHolder
import com.andrei.UI.bottomSheets.OptionsMessageBottomSheet
import com.andrei.carrental.R
import com.andrei.carrental.UserDataManager
import com.andrei.carrental.databinding.FragmentMessagesBinding
import com.andrei.carrental.entities.Message
import com.andrei.carrental.entities.MessageType
import com.andrei.carrental.viewmodels.ViewModelChat
import com.andrei.carrental.viewmodels.ViewModelUser
import com.andrei.engine.State
import com.andrei.messenger.MessengerService
import com.andreia.carrental.requestModels.CreateMessageRequest
import com.stfalcon.chatkit.commons.ImageLoader
import com.stfalcon.chatkit.messages.MessageHolders
import com.stfalcon.chatkit.messages.MessagesListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import pl.aprilapps.easyphotopicker.*
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class MessagesFragment :BaseFragment(R.layout.fragment_messages) ,
    MessageHolders.ContentChecker<Message>, MessagesListAdapter.OnLoadMoreListener {

    private val binding:FragmentMessagesBinding by viewBinding ()
    private val viewModelChat:ViewModelChat by activityViewModels()

    private val navArgs:MessagesFragmentArgs by navArgs()

    @Inject
    lateinit var messengerService: MessengerService

    @Inject
    lateinit var userDataManager: UserDataManager

    private val easyImage:EasyImage by lazy {
        EasyImage.Builder(requireContext()).allowMultiple(true).setChooserType(ChooserType.CAMERA_AND_GALLERY).build()
    }


    private val bottomSheet:OptionsMessageBottomSheet by lazy {
        OptionsMessageBottomSheet(this::unsendMessage).apply {
            isCancelable = false
        }
    }



    private  val imageLoader = ImageLoader { imageView, url, _ -> url?.let { imageView.load(it) } }


    private val messagesAdapter:CustomMessagesListAdapter by lazy {
          provideMessageAdapter()
    }


    private fun provideMessageAdapter(): CustomMessagesListAdapter {
       return CustomMessagesListAdapter(userDataManager.userID.toString(), getAdapterHolders(),imageLoader).apply {
            enableSelectionMode(CustomSelectionListener(userDataManager.userID.toString()))
            setLoadMoreListener(this@MessagesFragment)
        }
    }
    override fun onLoadMore(page: Int, totalItemsCount: Int) {
        lifecycleScope.launchWhenResumed {
            viewModelChat.loadMoreMessages(navArgs.chatID,totalItemsCount)
              viewModelChat.oldMessagesLoaded.collect {
                  if(it is State.Success){
                     messagesAdapter.addToEnd(it.data,true)
                  }
              }
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
                 viewModelChat.sendImages(imageFiles.asList(),navArgs.chatID,userDataManager.userID)
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
        bottomSheet.show(requireActivity().supportFragmentManager, "bottomSheetMessage")
    }



    override fun initializeUI() {
        configureToolbar()
        binding.messagesList.setAdapter(messagesAdapter)
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
          val activity =    requireActivity() as AppCompatActivity
            activity.apply {
                setSupportActionBar(binding.toolbar)
                supportActionBar?.setDisplayHomeAsUpEnabled(true)
                supportActionBar?.setDisplayShowHomeEnabled(true)
            }

        lifecycleScope.launchWhenResumed {
             val chat =  messengerService.getChat(navArgs.chatID)
             if(chat != null){
                 activity.supportActionBar?.title = chat.name
             }else{
                 activity.supportActionBar?.title = getString(R.string.unknown)
             }
        }
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }




    private fun sendTextMessage(messageText:String){
        val createMessageModel = CreateMessageRequest(
                senderID = userDataManager.userID,
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
            viewModelChat.getChatMessages(navArgs.chatID)
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


