package ru.samtakoy.core.data.local.database.room.converters

import androidx.room.TypeConverter
import org.apache.commons.lang3.StringUtils
import ru.samtakoy.core.app.utils.MyStringUtils
import java.util.*

class CardIdDequeConverter {

    @TypeConverter
    fun fromList(cardIds: Deque<Long>): String = MyStringUtils.join(cardIds.iterator(), ",")

    @TypeConverter
    fun fromString(srcString: String): Deque<Long> {

        var result = ArrayDeque<Long>()
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