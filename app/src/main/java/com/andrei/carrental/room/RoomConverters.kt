package com.andrei.carrental.room

import androidx.room.TypeConverter
import com.andrei.carrental.entities.Image
import com.andrei.carrental.entities.MessageType

class RoomConverters {

 @TypeConverter
 fun fromMessageTypeValue(messageTypeValue:Int?): MessageType?{
     messageTypeValue?.let { return when(messageTypeValue){
         MessageType.MESSAGE_RECEIVED_IMAGE.id -> MessageType.MESSAGE_RECEIVED_IMAGE
         MessageType.MESSAGE_SENT_IMAGE.id -> MessageType.MESSAGE_SENT_IMAGE
         MessageType.MESSAGE_RECEIVED_TEXT.id -> MessageType.MESSAGE_RECEIVED_TEXT
         else->MessageType.MESSAGE_SENT_TEXT
     } }
     return null
 }

    @TypeConverter
    fun messageTypeToValue(messageType: MessageType?):Int?{
        return messageType?.id
    }

    @TypeConverter
    fun fromImagePathToImage(imagePath:String?):Image?{
        return imagePath?.let {  Image(it) }
    }

    @TypeConverter
    fun fromImageFromPath(image: Image?):String?{
        return image?.imagePath
    }
}