package com.wings.android.todoapplication.presentation.di

import com.wings.android.todoapplication.data.ItemDao
import com.wings.android.todoapplication.data.ItemDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DaoModule {
    @Singleton
    @Provides
    fun provideItemDao(itemDatabase: ItemDatabase):ItemDao{
        return itemDatabase.getItemDao()
    }
}