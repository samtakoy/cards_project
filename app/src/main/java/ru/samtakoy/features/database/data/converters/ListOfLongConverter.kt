package ru.samtakoy.features.database.data.converters

import androidx.room.TypeConverter
import org.apache.commons.lang3.StringUtils
import ru.samtakoy.core.app.utils.MyStringUtils

class ListOfLongConverter {

    @TypeConverter
    fun fromList(cardIds: List<Long>): String = MyStringUtils.join(cardIds.iterator(), ",")

    @TypeConverter
    fun fromString(srcString: String): List<Long> {
        var result = ArrayList<Long>()
        val separated = StringUtils.split(srcString, ",")
        for (strNumber in separated) {
            result.add(java.lang.Long.valueOf(strNumber!!))
        }
        return result
    }

}