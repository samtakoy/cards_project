package ru.samtakoy.presentation.navigation

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.SaverScope
import kotlinx.serialization.json.Json

@Immutable
interface MainTabRoute

object MainTabRouteSaver : Saver<MainTabRoute, String> {
    override fun restore(value: String): MainTabRoute? {
        return try {
            Json.decodeFromString<MainTabRoute>(value)
        } catch (e: Exception) {
            null
        }
    }

    override fun SaverScope.save(value: MainTabRoute): String? {
        return Json.encodeToString(value)
    }
}