package ru.samtakoy.data.common.db.converters

import androidx.room.TypeConverter

const val IMAGES_DELIMITER = ','

internal class ImageListConverter {


    @TypeConverter
    fun fromString(serialized: String): List<String> {
        if (serialized.isEmpty()) {
            return ArrayList()
        }

        val parts = serialized.split(IMAGES_DELIMITER)
        val result: MutableList<String> = ArrayList(parts.size)
        for (img in parts) {
            result.add(img)
        }
        return result
    }

    @TypeConverter
    fun fromList(images: List<String>): String {
        return images.joinToString(IMAGES_DELIMITER.toString())
    }

}