package ru.samtakoy.core.database.room.converters

import androidx.room.TypeConverter
import org.apache.commons.lang3.StringUtils
import ru.samtakoy.core.utils.MyStringUtils

class CardIdListConverter {

    @TypeConverter
    fun fromList(cardIds: List<Long>): String = MyStringUtils.join(cardIds.iterator(), ",")

    @TypeConverter
    fun fromString(srcString: String): List<Long> {

        var result = ArrayList<Long>()
        /*if (srcString == null) {
            return result
        }*/

        val separated = StringUtils.split(srcString, ",")
        for (strNumber in separated) {
            result.add(java.lang.Long.valueOf(strNumber!!))
        }
        return result
    }

}