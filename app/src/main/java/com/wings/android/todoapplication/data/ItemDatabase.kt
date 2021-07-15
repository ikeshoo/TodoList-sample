package com.wings.android.todoapplication.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@TypeConverters(DateConverter::class)
@Database(
    entities = [Item::class],
    version = 1,
    exportSchema = false
)
abstract class ItemDatabase: RoomDatabase() {

   abstract fun getItemDao():ItemDao
}