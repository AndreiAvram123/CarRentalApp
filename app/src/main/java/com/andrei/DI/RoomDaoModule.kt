package com.andrei.DI

import android.content.Context
import com.andrei.carrental.room.LocalDatabase
import com.andrei.carrental.room.dao.MessageDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class RoomDaoModule {

  @Provides
  @Singleton
  fun provideRoomDatabase(@ApplicationContext context: Context):LocalDatabase = LocalDatabase.getDatabase(context)


  @Provides
  fun provideMessageDao(database: LocalDatabase):MessageDao = database.messageDao()

}