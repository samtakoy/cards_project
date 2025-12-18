package ru.samtakoy.presentation.main

import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableMap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.compose.getKoin
import ru.samtakoy.presentation.main.navigation.MainFlowRoute
import ru.samtakoy.presentation.main.navigation.changeTab
import ru.samtakoy.presentation.main.vm.MainScreenViewModel
import ru.samtakoy.presentation.navigation.MainTabFeatureEntry
import ru.samtakoy.presentation.navigation.MainTabRoute
import ru.samtakoy.presentation.navigation.RootFeatureEntry
import ru.samtakoy.presentation.navigation.TabRouteId
import ru.samtakoy.presentation.navigation.getRouteName
import ru.samtakoy.presentation.navigation.getRouteWithoutArgs
import ru.samtakoy.presentation.themes.list.ThemeListRoute
import ru.samtakoy.speech.presentation.TopPlayerView

@Composable
internal fun MainScreen(
    viewModel: MainScreenViewModel,
    modifier: Modifier = Modifier
) {
    val viewState by viewModel.getViewStateAsFlow().collectAsStateWithLifecycle()
    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        MainScreenInternal(
            viewState = viewState,
            onEvent = viewModel::onEvent
        )
        TopPlayerView(
            modifier = Modifier
                .systemBarsPadding()
        )
    }
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


    val koin = getKoin() // TODO через вью модель?
    val rootFeatureEntries: ImmutableList<RootFeatureEntry> = remember {
        koin.getAll<RootFeatureEntry>()
            .sortedBy { it::class.simpleName }
            .toImmutableList()
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

/** Отображение табов */
@Composable
private fun TabsView(
    viewState: MainScreenViewModel.State,
    onEvent: (MainScreenViewModel.Event) -> Unit,
    rootNavController: NavHostController,
    drawerState: DrawerState,
    coroutineScope: CoroutineScope
) {
    val koin = getKoin() // TODO через вью модель?
    val mainTabFeatureEntries: ImmutableList<MainTabFeatureEntry> = remember {
        koin.getAll<MainTabFeatureEntry>().toImmutableList()
    }
    val mainTabFeatureEntriesById: ImmutableMap<TabRouteId, MainTabFeatureEntry> = remember(mainTabFeatureEntries) {
        buildMap {
            mainTabFeatureEntries.forEach { featureEntry ->
                put(featureEntry.tabId, featureEntry)
            }
        }.toImmutableMap()
    }

    val tabsNavController = rememberNavController()
    val currentBackStack by tabsNavController.currentBackStack.collectAsState()
    val currentRootRouteName = remember {
        derivedStateOf {
            currentBackStack.find { it.destination.route.isNullOrEmpty().not() }?.destination?.route?.getRouteWithoutArgs()
        }
    }

    LaunchedEffect(viewState.selectedItemId) {
        // синхронизация навигационного графа при изменении viewState.selectedItemId
        val featureEntry = mainTabFeatureEntriesById[viewState.selectedItemId] ?: return@LaunchedEffect
        val currentRoot = currentRootRouteName.value ?: return@LaunchedEffect
        if (currentRoot.startsWith(featureEntry.getRouteName()).not()) {
            tabsNavController.changeTab(featureEntry.defaultRoute)
        }
    }

    LaunchedEffect(currentRootRouteName) {
        // при изменении таба синхронизация viewState.selectedItemId
        val currentRoot = currentRootRouteName.value ?: return@LaunchedEffect
        val featureEntry = mainTabFeatureEntriesById[viewState.selectedItemId] ?: return@LaunchedEffect
        if (currentRoot.startsWith(featureEntry.getRouteName()).not()) {
            onEvent(MainScreenViewModel.Event.NavigationChangedExternally(featureEntry.tabId))
        }
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
        NavHost(
            navController = tabsNavController,
            startDestination = resolveStartDestination(mainTabFeatureEntries),
            modifier = Modifier.fillMaxSize()
        ) {
            mainTabFeatureEntries.forEach { featureEntry ->
                featureEntry.registerGraph(
                    navGraphBuilder = this,
                    rootNavController = rootNavController,
                    currentNavController = tabsNavController,
                    onMainNavigator = {
                        coroutineScope.launch { drawerState.open() }
                    }
                )
            }
        }
    }
}

@Composable
private fun resolveStartDestination(mainTabFeatureEntries: ImmutableList<MainTabFeatureEntry>): MainTabRoute {
    return remember {
        mainTabFeatureEntries.find { it.defaultRoute is ThemeListRoute }!!.defaultRoute
    }
}