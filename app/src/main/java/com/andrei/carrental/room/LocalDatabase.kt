package com.andrei.carrental.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.andrei.carrental.entities.Message
import com.andrei.carrental.room.dao.ChatDao
import com.andrei.carrental.room.dao.MessageDao
import com.andrei.engine.DTOEntities.Chat
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.synchronized

@Database(entities = [Message::class, Chat::class], version = 7, exportSchema = false)
@TypeConverters(RoomConverters::class)
abstract class LocalDatabase : RoomDatabase() {

    abstract fun messageDao(): MessageDao
    abstract fun chatDao():ChatDao


    companion object {
        @Volatile
        private var INSTANCE: LocalDatabase? = null

        @OptIn(InternalCoroutinesApi::class)
        fun getDatabase(application: Context): LocalDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(application,
                        LocalDatabase::class.java,
                        "database").fallbackToDestructiveMigration()
                        .enableMultiInstanceInvalidation()
                        .build()
                INSTANCE = instance
                return instance
            }

        }
    }
}