package ru.samtakoy.desktop

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.ApplicationScope
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.example.maindi.koinModulesModule
import io.github.aakira.napier.Napier
import io.github.vinceglb.filekit.FileKit
import org.koin.compose.KoinApplication
import org.koin.compose.koinInject
import ru.samtakoy.common.utils.log.CustomLogger
import ru.samtakoy.presentation.main.MainScreenEntry

fun main() = application {

    Window(
        onCloseRequest = ::exitApplication,
        title = "Your App Name",
        state = rememberWindowState(width = 800.dp, height = 600.dp)
    ) {
        KoinApplication(
            application = {
                // TODO мои модули
                modules(koinModulesModule())
            }
        ) {
            initLibs()
            MainScreenEntry()
        }
    }
}

@Composable
private fun ApplicationScope.initLibs() {
    val logger: CustomLogger = koinInject()
    Napier.base(logger)
   // Инициализация FileKit с вашим ID приложения (appId) - TODO взять из gradle?
    FileKit.init("ru.samtakoy.cards")
}