package com.andrei.carrental.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.andrei.engine.DTOEntities.Chat
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull

@Dao
interface ChatDao {
    @Query("SELECT * FROM chat")
    fun findAllChats(): Flow<List<Chat>>

    fun findAllChatsDistinct() = findAllChats().distinctUntilChanged().filterNotNull()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChat(chat: Chat)
}