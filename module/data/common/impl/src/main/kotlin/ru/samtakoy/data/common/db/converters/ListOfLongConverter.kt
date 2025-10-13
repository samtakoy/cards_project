package ru.samtakoy.data.common.db.converters

import androidx.room.TypeConverter
import ru.samtakoy.common.utils.MyStringUtils

internal class ListOfLongConverter {

    @TypeConverter
    fun fromList(cardIds: List<Long>): String = MyStringUtils.join(cardIds.iterator(), ",")

    @TypeConverter
    fun fromString(srcString: String): List<Long> {
        var result = ArrayList<Long>()
        val separated = srcString.split(',')
        for (strNumber in separated) {
            result.add(java.lang.Long.valueOf(strNumber))
        }
        return result
    }

}