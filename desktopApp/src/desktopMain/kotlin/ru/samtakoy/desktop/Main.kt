package ru.samtakoy.desktop

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
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
                modules(koinModulesModule())
            }
        ) {
            initLibs()
            MainScreenEntry()
        }
    }
}

/** TODO подумать */
@Composable
private fun initLibs() {
    val logger: CustomLogger = koinInject()
    val initialized = remember { mutableStateOf(false) }
    DisposableEffect(Unit) {
        if (!initialized.value) {
            Napier.base(logger)
            // Инициализация FileKit с вашим ID приложения (appId) - TODO взять из gradle?
            FileKit.init("ru.samtakoy.cards")
            initialized.value = true
        }
    onDispose {}
    }
}