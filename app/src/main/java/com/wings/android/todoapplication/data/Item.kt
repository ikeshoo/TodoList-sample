package com.wings.android.todoapplication.data


import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.time.LocalDate

@Entity(tableName = "items")
data class Item(
    @PrimaryKey(autoGenerate = true)
    val id:Int = 0,
    var content:String,
    val dateOfWrite:LocalDate,
    var deadline:LocalDate,
):Serializable