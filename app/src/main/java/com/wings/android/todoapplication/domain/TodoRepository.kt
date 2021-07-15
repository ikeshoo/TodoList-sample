package com.wings.android.todoapplication.domain

import com.wings.android.todoapplication.data.Item
import com.wings.android.todoapplication.data.ItemDao
import kotlinx.coroutines.flow.Flow

class TodoRepository(
    private val itemDao: ItemDao
) {

    suspend fun saveItem(item: Item): Long {
        return itemDao.insertItem(item)
    }

    fun getItems(): Flow<List<Item>> {
        return itemDao.getItems()
    }

    suspend fun deleteItem(item: Item) {
        itemDao.deleteItem(item)
    }

    suspend fun updateItem(item: Item) {
        itemDao.updateItem(item)
    }

    fun getItemsByDate(): Flow<List<Item>> {
        return itemDao.getItemsByDate()
    }

    fun getItemsByDeadline(): Flow<List<Item>> {
        return itemDao.getItemsByDeadline()
    }

    fun searchItems(query: String): Flow<List<Item>> {
        val concatQuery = "%$query%"
        return itemDao.searchItems(concatQuery)
    }
}