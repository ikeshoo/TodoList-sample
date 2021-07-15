package com.wings.android.todoapplication.presentation.di

import com.wings.android.todoapplication.domain.TodoRepository
import com.wings.android.todoapplication.presentation.viewmodel.TodoViewModelFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class ViewModelFactoryModule {

    @Singleton
    @Provides
    fun provideTodoViewModelFactory(todoRepository: TodoRepository):TodoViewModelFactory{
        return TodoViewModelFactory(todoRepository)
    }
}