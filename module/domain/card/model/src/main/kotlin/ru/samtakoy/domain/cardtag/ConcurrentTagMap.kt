package ru.samtakoy.domain.cardtag

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class ConcurrentTagMap {

    private val mutext = Mutex()

    private val keyToTagMap = mutableMapOf<String, Tag>()

    suspend fun addTags(tags: List<Tag>) = mutext.withLock {
        tags.forEach { oneTag ->
            keyToTagMap[tagToKey(oneTag)] = oneTag
        }
    }

    suspend fun addTags(vararg tag: Tag) = mutext.withLock {
        tag.forEach { oneTag ->
            keyToTagMap[tagToKey(oneTag)] = oneTag
        }
    }

    suspend fun getByKey(key: String): Tag? = mutext.withLock {
        return@withLock keyToTagMap[key]
    }

    suspend fun getByKeys(keys: List<String>): List<Tag> = mutext.withLock {
        return@withLock keys.mapNotNull { keyToTagMap[it] }
    }

    suspend fun addIfNone(
        key: String,
        elementFactory: () -> Tag
    ): Boolean = mutext.withLock {
        return@withLock if (keyToTagMap[key] == null) {
            keyToTagMap[key] = elementFactory()
            true
        } else {
            false
        }
    }

    suspend fun getNewTags(): List<Tag> = mutext.withLock {
        return@withLock keyToTagMap.values.asSequence().filter { it.id == TagConst.NO_ID }.toList()
    }

    private fun tagToKey(tag: Tag): String {
        return tag.toStringKey()
    }
}