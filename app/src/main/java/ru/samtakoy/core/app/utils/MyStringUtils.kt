package ru.samtakoy.core.app.utils

object MyStringUtils {
    private const val EMPTY = ""

    fun join(iterable: Iterable<*>, separator: String): String {
        return MyStringUtils.join(iterable.iterator(), separator)
    }

    fun join(iterator: Iterator<*>, separator: String): String {
        // handle null, zero and one elements before building a buffer
        if (!iterator.hasNext()) {
            return EMPTY
        }
        val first = iterator.next()
        if (!iterator.hasNext()) {
            return first.toString()
        }

        // two or more elements
        val buf = StringBuilder() // Java default is 16, probably too small
        if (first != null) {
            buf.append(first)
        }

        while (iterator.hasNext()) {
            if (separator != null) {
                buf.append(separator)
            }
            val obj = iterator.next()
            if (obj != null) {
                buf.append(obj)
            }
        }
        return buf.toString()
    }
}
