package ru.samtakoy.presentation.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.compose.currentKoinScope
import ru.samtakoy.presentation.main.navigation.MainFlowRoute
import ru.samtakoy.presentation.main.vm.MainScreenViewModel
import ru.samtakoy.presentation.navigation.MainTabFeatureEntry
import ru.samtakoy.presentation.navigation.RootFeatureEntry
import ru.samtakoy.presentation.themes.list.ThemeListRoute

@Composable
internal fun MainScreen(
    viewModel: MainScreenViewModel,
    modifier: Modifier = Modifier
) {
    val viewState by viewModel.getViewStateAsFlow().collectAsStateWithLifecycle()
    MainScreenInternal(
        viewState = viewState,
        onEvent = viewModel::onEvent,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainScreenInternal(
    viewState: MainScreenViewModel.State,
    onEvent: (MainScreenViewModel.Event) -> Unit,
    modifier: Modifier = Modifier
) {
    val rootNavController = rememberNavController()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    val koin = currentKoinScope()
    val rootFeatureEntries: ImmutableList<RootFeatureEntry> = remember {
        koin.getAll<RootFeatureEntry>().toImmutableList()
    }

    NavHost(
        navController = rootNavController,
        startDestination = MainFlowRoute.Tabs,
        modifier = modifier.fillMaxSize()
    ) {
        composable<MainFlowRoute.Tabs> {
            TabsView(
                viewState = viewState,
                onEvent = onEvent,
                rootNavController = rootNavController,
                drawerState = drawerState,
                coroutineScope = coroutineScope
            )
        }
        rootFeatureEntries.forEach { featureEntry ->
            featureEntry.registerGraph(
                navGraphBuilder = this,
                rootNavController = rootNavController
            )
        }
    }
}

@Composable
private fun TabsView(
    viewState: MainScreenViewModel.State,
    onEvent: (MainScreenViewModel.Event) -> Unit,
    rootNavController: NavHostController,
    drawerState: DrawerState,
    coroutineScope: CoroutineScope
) {
    val koin = currentKoinScope()
    val mainTabFeatureEntries: ImmutableList<MainTabFeatureEntry> = remember {
        koin.getAll<MainTabFeatureEntry>().toImmutableList()
    }

    ModalNavigationDrawer(
        modifier = Modifier.systemBarsPadding(),
        drawerContent = {
            ModalDrawerSheet {
                Column {
                    viewState.menuItems.forEach { menuItem ->
                        key(menuItem.id) {
                            NavigationDrawerItem(
                                label = { Text(menuItem.title) },
                                selected = menuItem.id == viewState.selectedItemId,
                                onClick = {
                                    coroutineScope.launch { drawerState.close() }
                                    onEvent(MainScreenViewModel.Event.MenuItemClick(menuItem))
                                }
                            )
                        }
                    }
                }
            }
        },
        drawerState = drawerState,
    ) {
        val tabsNavController = rememberNavController()
        NavHost(
            navController = tabsNavController,
            startDestination = remember { mainTabFeatureEntries.find { it.route is ThemeListRoute }!!.route },
            modifier = Modifier.fillMaxSize()
        ) {
            mainTabFeatureEntries.forEach { featureEntry ->
                featureEntry.registerGraph(
                    navGraphBuilder = this,
                    rootNavController = rootNavController,
                    tabsNavController = tabsNavController,
                    onMainNavigator = {
                        coroutineScope.launch { drawerState.open() }
                    }
                )
            }
        }
    }
}

fun NavController.isTabsVisible(): Boolean {
    return try {
        getBackStackEntry(MainFlowRoute.Tabs)
        true
    } catch (_: Throwable) {
        false
    }
}
