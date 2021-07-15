package com.wings.android.todoapplication.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {

    @Query("SELECT * FROM items")
    fun getItems():Flow<List<Item>>

    @Query("SELECT * FROM items ORDER BY dateOfWrite ASC")
    fun getItemsByDate():Flow<List<Item>>

    @Query("SELECT * FROM items ORDER BY deadline ASC")
    fun getItemsByDeadline():Flow<List<Item>>

    @Query("SELECT * FROM items WHERE content LIKE :concatQuery ")
    fun searchItems(concatQuery:String):Flow<List<Item>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: Item):Long

    @Delete
    suspend fun deleteItem(item: Item)

    @Update
    suspend fun updateItem(item: Item)
}