package com.wings.android.todoapplication.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.wings.android.todoapplication.domain.TodoRepository

class TodoViewModelFactory(
    private val todoRepository: TodoRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return TodoViewModel(todoRepository) as T
    }
}