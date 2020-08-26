package ru.samtakoy.core.database.room.converters

import androidx.room.TypeConverter
import org.apache.commons.lang3.StringUtils
import ru.samtakoy.core.utils.MyStringUtils
import java.util.*

const val IMAGES_DELIMITER = ","

class ImageListConverter {


    @TypeConverter
    fun fromString(serialized: String): List<String> {
        if (serialized.isEmpty()) {
            return ArrayList()
        }

        val parts = StringUtils.split(serialized, IMAGES_DELIMITER)
        val result: MutableList<String> = ArrayList(parts.size)
        for (img in parts) {
            result.add(img)
        }
        return result
    }

    @TypeConverter
    fun fromList(images: List<String>): String {
        return MyStringUtils.join(images, IMAGES_DELIMITER)
    }

}