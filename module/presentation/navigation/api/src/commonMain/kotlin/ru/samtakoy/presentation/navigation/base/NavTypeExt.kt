package ru.samtakoy.presentation.navigation.base

import androidx.navigation.NavType
import androidx.savedstate.SavedState
import androidx.savedstate.read
import androidx.savedstate.write
import kotlinx.serialization.json.Json

/*
abstract class BaseNavType<T>(
    isNullableAllowed: Boolean = false
) : NavType<T>(isNullableAllowed = isNullableAllowed) {

    override fun put(bundle: SavedState, key: String, value: T) {
        bundle.write { putString(key, serializeAsValue(value)) }
    }

    override fun get(bundle: SavedState, key: String): T? {
        return bundle.read<String?> { getString(key) }?.let { parseValue(it) }
    }

    override fun parseValue(value: String): T {
        return fromString(value)
    }

    override fun serializeAsValue(value: T): String {
        return toString(value)
    }

    abstract fun toString(value: T): String

    abstract fun fromString(value: String): T
}

*/

inline fun <reified T : Any> navType(
    isNullableAllowed: Boolean = false,
    json: Json = Json,
) = object : NavType<T>(isNullableAllowed = isNullableAllowed) {

    override fun put(bundle: SavedState, key: String, value: T) {
        bundle.write { putString(key, serializeAsValue(value)) }
    }

    override fun get(bundle: SavedState, key: String): T? {
        return bundle.read<String?> { getString(key) }?.let { parseValue(it) }
    }

    override fun parseValue(value: String): T {
        return Json.decodeFromString(value)
    }

    override fun serializeAsValue(value: T): String {
        return json.encodeToString(value)
    }
}