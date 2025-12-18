package ru.samtakoy.presentation.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import org.koin.android.ext.android.inject
import org.koin.compose.viewmodel.koinViewModel
import ru.samtakoy.platform.permissions.PermissionsControllerImplAndroid
import ru.samtakoy.presentation.core.design_system.base.theme.MyTheme
import ru.samtakoy.presentation.main.vm.MainScreenViewModelImpl

class ComposeActivity : ComponentActivity() {

    private val permissionsController: PermissionsControllerImplAndroid by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        permissionsController.bind(this)
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            MyTheme {
                MainScreen(
                    viewModel = koinViewModel<MainScreenViewModelImpl>(),
                    modifier = Modifier.Companion.fillMaxSize()
                )
            }
        }
    }

    override fun onDestroy() {
        permissionsController.unbind()
        super.onDestroy()
    }
}