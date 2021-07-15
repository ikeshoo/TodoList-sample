package com.wings.android.todoapplication.presentation.di

import android.app.Application
import androidx.room.Room
import com.wings.android.todoapplication.data.ItemDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideItemDatabase(app:Application):ItemDatabase{
        return Room.databaseBuilder(app,ItemDatabase::class.java,"todo_db")
            .build()
    }
}