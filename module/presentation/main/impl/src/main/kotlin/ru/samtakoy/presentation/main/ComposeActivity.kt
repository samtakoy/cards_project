package ru.samtakoy.presentation.main

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.samtakoy.platform.permissions.PermissionsControllerImplAndroid
import ru.samtakoy.presentation.core.design_system.base.theme.MyTheme
import ru.samtakoy.presentation.main.vm.MainScreenViewModel
import ru.samtakoy.presentation.main.vm.MainScreenViewModelImpl

class ComposeActivity : AppCompatActivity() {

    private val viewModel: MainScreenViewModel by viewModel<MainScreenViewModelImpl>()
    private val permissionsController: PermissionsControllerImplAndroid by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        permissionsController.bind(this)
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            MyTheme {
                MainScreen(
                    viewModel = viewModel,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }

    override fun onDestroy() {
        permissionsController.unbind()
        super.onDestroy()
    }
}