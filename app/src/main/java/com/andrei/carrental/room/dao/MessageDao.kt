package com.andrei.carrental.room.dao

import androidx.room.*
import com.andrei.carrental.entities.Message
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.distinctUntilChangedBy

@Dao
interface MessageDao{
     @Query("SELECT * FROM message WHERE chatID = :chatID AND type IS NOT 3 ORDER BY date DESC LIMIT 1")
     fun findLastChatMessage(chatID:Long): Flow<Message>





    @Query("SELECT * FROM message WHERE chatID = :chatID ORDER BY date DESC LIMIT 20")
    suspend fun findLastChatMessages(chatID:Long):List<Message>


    @Update
    suspend fun updateMessage(message: Message)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
     suspend fun insertMessage(message: Message)

     @Insert(onConflict = OnConflictStrategy.REPLACE)
     suspend fun insertMessages(messages:List<Message>)

     @Delete
     suspend fun deleteMessage(message: Message)

     @Query("DELETE FROM message")
     suspend fun clean()
}

