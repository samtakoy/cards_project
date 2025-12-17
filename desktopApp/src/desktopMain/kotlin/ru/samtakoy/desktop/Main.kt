package ru.samtakoy.desktop

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.example.maindi.koinModulesModule
import io.github.aakira.napier.Napier
import io.github.vinceglb.filekit.FileKit
import org.koin.core.context.startKoin
import ru.samtakoy.common.utils.log.CustomLogger
import ru.samtakoy.presentation.main.MainScreenEntry

fun main() {
    initLibs()
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "Your App Name",
            state = rememberWindowState(width = 800.dp, height = 600.dp)
        ) {
            MainScreenEntry()
        }
    }
}

private fun initLibs() {
    val koinApp = startKoin {
        modules(koinModulesModule())
    }
    Napier.base(koinApp.koin.get<CustomLogger>())
    // Инициализация FileKit с вашим ID приложения (appId) - TODO взять из gradle?
    FileKit.init("ru.samtakoy.cards")
}