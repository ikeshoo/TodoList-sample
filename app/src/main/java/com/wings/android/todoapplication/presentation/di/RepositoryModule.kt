package com.wings.android.todoapplication.presentation.di

import com.wings.android.todoapplication.data.ItemDao
import com.wings.android.todoapplication.domain.TodoRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class RepositoryModule {

    @Singleton
    @Provides
    fun provideTodoRepository(itemDao: ItemDao):TodoRepository{
        return TodoRepository(itemDao)
    }
}