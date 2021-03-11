package com.andrei.carrental.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.andrei.carrental.entities.Message
import com.andrei.carrental.room.dao.MessageDao
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.synchronized

@Database(entities = [Message::class], version = 4, exportSchema = false)
@TypeConverters(RoomConverters::class)
abstract class LocalDatabase : RoomDatabase() {

    abstract fun messageDao(): MessageDao


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