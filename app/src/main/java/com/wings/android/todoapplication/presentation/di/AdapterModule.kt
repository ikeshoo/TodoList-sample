package com.wings.android.todoapplication.presentation.di

import com.wings.android.todoapplication.presentation.adapter.ItemAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class AdapterModule {
    @Singleton
    @Provides
    fun provideItemAdapter():ItemAdapter{
        return ItemAdapter()
    }
}