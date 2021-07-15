package com.wings.android.todoapplication.presentation.di

import com.wings.android.todoapplication.presentation.dialog.DatePickerFragment
import com.wings.android.todoapplication.presentation.dialog.TimePickerFragment
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.scopes.FragmentScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(FragmentComponent::class)
@Module
class PickerFragmentModule {

    @FragmentScoped
    @Provides
    fun provideDatePickerFragment():DatePickerFragment{
        return DatePickerFragment()
    }

    @FragmentScoped
    @Provides
    fun provideTimePickerFragment():TimePickerFragment{
        return TimePickerFragment()
    }
}