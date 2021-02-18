package com.andrei.UI.fragments

import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.andrei.UI.bottomSheets.OptionsMessageBottomSheet
import com.andrei.carrental.R
import com.andrei.carrental.UserDataManager
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
import java.util.logging.Handler
import javax.inject.Inject


@AndroidEntryPoint
class MessagesFragment :BaseFragment(R.layout.fragment_messages) {

    private val binding:FragmentMessagesBinding by viewBinding ()
    private val viewModelChat:ViewModelChat by activityViewModels()
    private val navArgs:MessagesFragmentArgs by navArgs()
    private var skipScroll: Boolean = false

    @Inject
    lateinit var messengerService: MessengerService

    private val bottomSheet:OptionsMessageBottomSheet by lazy {
        OptionsMessageBottomSheet(this::unsendMessage).apply {
            isCancelable = false
        }
    }

     private  val imageLoader = ImageLoader { imageView, url, _ -> url?.let { imageView.loadFromURl(it) } }




    private val messagesAdapter:MessagesListAdapter<Message> by lazy {
        MessagesListAdapter<Message>(UserDataManager.getUserID(requireContext()).toString(),imageLoader).also {
            configureMessageAdapter()
        }
    }


    inner class CustomSelectionListener(private val currentUserID : String):MessagesListAdapter.SelectionListener{
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



    private fun configureMessageAdapter() {
        lifecycleScope.launch(Dispatchers.Main) {
            messagesAdapter.apply {
                enableSelectionMode(CustomSelectionListener(UserDataManager.getUserID(requireContext()).toString()))
            }
        }
    }

    private fun unsendMessage(){
        dismissBottomSheet()
        val messages =   messagesAdapter.selectedMessages.first()
        messagesAdapter.unselectAllItems()
        viewModelChat.setMessageToUnsend(messages)
    }
    private fun showBottomSheet() {
        skipScroll = true
        bottomSheet.show(requireActivity().supportFragmentManager, "bottomSheetMessage")
    }
    private fun dismissBottomSheet(){
        bottomSheet.dismiss()
        android.os.Handler(Looper.getMainLooper()).postDelayed({
            skipScroll = false
        },200)
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
            if (shouldScroll(bottom,oldBottom)) {
                binding.messagesList.postDelayed({
                    binding.messagesList.smoothScrollToPosition(0)
                }, 50)
            }
        }
    }

    private fun shouldScroll(bottom:Int, oldBottom:Int):Boolean{
        if(skipScroll){
            return false
        }
        return bottom < oldBottom && messagesAdapter.selectedMessages.isEmpty()
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


