package ru.samtakoy.iosumbrella

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.ComposeUIViewController
import com.example.maindi.koinModulesModule
import io.github.aakira.napier.Napier
import org.koin.compose.KoinApplication
import org.koin.compose.koinInject
import ru.samtakoy.common.utils.log.CustomLogger
import ru.samtakoy.presentation.main.MainScreenEntry


fun MainViewController() = ComposeUIViewController { AppEntryPoint() }

@Composable
private fun AppEntryPoint() {
    KoinApplication(
        application = {
            modules(koinModulesModule())
        }
    ) {
        initLibs()
        MainScreenEntry()
    }
}

@Composable
private fun initLibs() {
    val logger: CustomLogger = koinInject()
    Napier.base(logger)
}