package com.wings.android.todoapplication.data

import androidx.room.TypeConverter
import java.time.LocalDate

class DateConverter {

    /**
     * LocalDate->文字列変換
     * フォーマットはyyyy-MM-dd
     */
    @TypeConverter
    fun fromLocalDate(localDate: LocalDate):String{
        return localDate.toString()
    }

    /**
     * 文字列->LocalDate
     */
    @TypeConverter
    fun toLocalDate(stringDate:String):LocalDate{
        return LocalDate.parse(stringDate)
    }
}