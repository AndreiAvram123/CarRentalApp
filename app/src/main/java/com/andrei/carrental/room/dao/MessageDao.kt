package com.andrei.carrental.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.andrei.carrental.entities.Message

@Dao
interface MessageDao{
     @Query("SELECT * FROM message WHERE chatID = :chatID ORDER BY date DESC LIMIT 1")
     fun findLastChatMessage(chatID:Long):LiveData<Message>


    @Query("SELECT * FROM message WHERE chatID = :chatID ORDER BY date ASC LIMIT 20")
    suspend fun findLastChatMessages(chatID:Long):List<Message>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
     suspend fun insertMessage(message: Message)

     @Insert(onConflict = OnConflictStrategy.REPLACE)
     suspend fun insertMessages(messages:List<Message>)

     @Delete
     suspend fun deleteMessage(message: Message)

     @Query("DELETE FROM message")
     suspend fun clean()
}
