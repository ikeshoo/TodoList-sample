package com.wings.android.todoapplication.presentation.viewmodel

import android.app.DownloadManager
import android.util.Log
import androidx.lifecycle.*
import com.wings.android.todoapplication.data.Item
import com.wings.android.todoapplication.domain.TodoRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.time.LocalDate

class TodoViewModel(
    private val todoRepository: TodoRepository
):ViewModel() {
    var insertRowId:Int? = null
    val displayContent = MutableLiveData<String>()
    val displayDeadline = MutableLiveData<String>()


    fun getSavedItems() = liveData {
        todoRepository.getItems().collect {
            emit(it)
        }
    }

    suspend fun saveItem() = coroutineScope{
        val insertItem = Item(
            0,
            displayContent.value!!,
            LocalDate.now(),
            LocalDate.parse(displayDeadline.value)
        )
        insertRowId =  todoRepository.saveItem(insertItem).toInt()
    }




    fun deleteItem(item: Item) = viewModelScope.launch {
        todoRepository.deleteItem(item)
    }

    suspend fun updateItem(item: Item) = coroutineScope {
        item.content = displayContent.value!!
        item.deadline = LocalDate.parse(displayDeadline.value.toString())

        todoRepository.updateItem(item)
    }

    fun getSavedItemsByDate() = liveData {
        todoRepository.getItemsByDate().collect {
            emit(it)
        }
    }

    fun getSavedItemsByDeadline() = liveData {
        todoRepository.getItemsByDeadline().collect {
            emit(it)
        }
    }

    fun getSearchedItems(query: String) = liveData {
        todoRepository.searchItems(query).collect {
            emit(it)
        }
    }

    fun setMakeItemText(item:Item){
        displayContent.value = item.content
        displayDeadline.value = item.deadline.toString()
    }

    fun initializeMakiItemText(){
        displayContent.value = ""
        displayDeadline.value = ""
    }


}