package com.andrei.carrental.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.andrei.carrental.entities.Message

@Dao
interface MessageDao{
     @Query("SELECT * FROM message WHERE chatID = :chatID ORDER BY date DESC")
     fun findLastChatMessage(chatID:Long):LiveData<Message>


    @Query("SELECT * FROM message WHERE chatID = :chatID ORDER BY date ASC LIMIT 20")
    suspend fun findLastChatMessages(chatID:Long):List<Message>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
     fun insertMessage(message: Message)

     @Insert(onConflict = OnConflictStrategy.REPLACE)
     suspend fun insertMessages(messages:List<Message>)
}
