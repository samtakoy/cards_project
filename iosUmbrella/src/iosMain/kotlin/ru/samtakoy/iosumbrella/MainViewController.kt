package ru.samtakoy.iosumbrella

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.ComposeUIViewController
import com.example.maindi.koinModulesModule
import io.github.aakira.napier.Napier
import org.koin.core.context.startKoin
import ru.samtakoy.common.utils.log.CustomLogger
import ru.samtakoy.presentation.main.MainScreenEntry
import org.koin.mp.KoinPlatform

fun MainViewController() = ComposeUIViewController { AppEntryPoint() }

object IOSInitializer {
    fun inititialize() {
        if (KoinPlatform.getKoinOrNull() == null) {
            val koinApp = startKoin {
                modules(koinModulesModule())
            }
            Napier.base(KoinPlatform.getKoin().get<CustomLogger>())
        }
    }
}

@Composable
private fun AppEntryPoint() {
    MainScreenEntry()
}