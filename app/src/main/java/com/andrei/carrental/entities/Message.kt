package com.andrei.carrental.entities

import androidx.room.*
import com.stfalcon.chatkit.commons.models.IMessage
import com.stfalcon.chatkit.commons.models.IUser
import com.stfalcon.chatkit.commons.models.MessageContentType
import java.util.*

@Entity
data class Message(
        @PrimaryKey
    @ColumnInfo(name = "messageID")
    val messageID:Long,
        @ColumnInfo(name = "content")
    val textContent:String?,
        @ColumnInfo(name = "date")
    val date:Long,
        @Embedded
    val sender:User,
        @ColumnInfo(name = "chatID")
    val chatID:Long,
        @ColumnInfo(name=  "mediaURL")
    val mediaURL:String?,
        @ColumnInfo(name = "type")
        var messageType:MessageType,

):IMessage, MessageContentType,MessageContentType.Image{

    @Ignore
    override fun getText(): String  = textContent ?: ""
    @Ignore
    override fun getCreatedAt(): Date = Date(date)
    @Ignore
    override fun getId(): String  = messageID.toString()
    @Ignore
    override fun getUser(): IUser = sender

    @Ignore
    override fun getImageUrl(): String? {
        return if(messageType == MessageType.MESSAGE_IMAGE){
            mediaURL
        }
        else null
    }


}

enum class MessageType(val id: Int) {
   MESSAGE_TEXT(1), MESSAGE_IMAGE(2), MESSAGE_UNSENT(3)

}



