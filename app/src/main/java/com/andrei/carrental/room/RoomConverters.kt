package com.andrei.carrental.room

import androidx.room.TypeConverter
import com.andrei.carrental.entities.Image
import com.andrei.carrental.entities.MessageType

class RoomConverters {

 @TypeConverter
 fun fromMessageTypeValue(messageTypeValue:Int?): MessageType? {
     messageTypeValue?.let {
         return when (messageTypeValue) {
             MessageType.MESSAGE_TEXT.id -> MessageType.MESSAGE_TEXT
             MessageType.MESSAGE_IMAGE.id -> MessageType.MESSAGE_IMAGE
             MessageType.MESSAGE_UNSENT.id -> MessageType.MESSAGE_UNSENT
             else -> null
         }
     }
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
