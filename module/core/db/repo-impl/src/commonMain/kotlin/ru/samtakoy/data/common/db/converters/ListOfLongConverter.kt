package ru.samtakoy.data.common.db.converters

import androidx.room.TypeConverter

internal class ListOfLongConverter {

    @TypeConverter
    fun fromList(cardIds: List<Long>): String = cardIds.joinToString(",")

    @TypeConverter
    fun fromString(srcString: String): List<Long> {
        return srcString.split(',').mapNotNull { it.toLongOrNull() }
    }

}