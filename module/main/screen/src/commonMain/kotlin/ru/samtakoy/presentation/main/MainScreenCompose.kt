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
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.NonRestartableComposable
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
import kotlinx.coroutines.launch
import org.koin.compose.getKoin
import ru.samtakoy.navigation.presentation.LocalRootNavigator
import ru.samtakoy.navigation.presentation.RootFeatureEntry
import ru.samtakoy.navigation.presentation.RootNavigator
import ru.samtakoy.navigation.presentation.RootRoute
import ru.samtakoy.presentation.base.observeActionsWithLifecycle
import ru.samtakoy.presentation.main.navigation.MainFlowRoute
import ru.samtakoy.presentation.main.vm.MainScreenViewModel
import ru.samtakoy.presentation.themes.list.ThemeListRoute
import ru.samtakoy.speech.presentation.TopPlayerView
import ru.samtakoy.tabnavigation.presentation.MainTabFeatureEntry
import ru.samtakoy.tabnavigation.presentation.getRouteName
import ru.samtakoy.tabnavigation.presentation.getRouteWithoutArgs
import ru.samtakoy.tabnavigation.presentation.model.MainTabRoute
import ru.samtakoy.tabnavigation.presentation.model.TabRouteId
import ru.samtakoy.tabnavigation.presentation.navigator.LocalTabNavigator
import ru.samtakoy.tabnavigation.presentation.navigator.TabNavigator
import ru.samtakoy.tabnavigation.presentation.utils.changeTab

@Composable
internal fun MainScreen(
    viewModel: MainScreenViewModel,
    modifier: Modifier = Modifier
) {
    val viewState by viewModel.getViewStateAsFlow().collectAsStateWithLifecycle()
    val tabsNavController = rememberNavController()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        MainScreenInternal(
            viewState = viewState,
            onEvent = viewModel::onEvent,
            drawerState = drawerState,
            tabsNavController = tabsNavController,
            tabNavigator = viewModel.tabNavigator
        )
        TopPlayerView(
            modifier = Modifier
                .systemBarsPadding()
        )
    }
    HandleActions(
        viewModel = viewModel,
        drawerState = drawerState,
        tabsNavController = tabsNavController
    )
}

@Composable
private fun HandleActions(
    viewModel: MainScreenViewModel,
    drawerState: DrawerState,
    tabsNavController: NavHostController
) {
    val coroutineScope = rememberCoroutineScope()
    viewModel.getViewActionsAsFlow().observeActionsWithLifecycle { action ->
        when (action) {
            MainScreenViewModel.Action.ShowMainDrawer -> {
                coroutineScope.launch {
                    drawerState.open()
                }
            }
            is MainScreenViewModel.Action.AddTabFlowScreen -> {
                tabsNavController.navigate(action.route)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainScreenInternal(
    viewState: MainScreenViewModel.State,
    onEvent: (MainScreenViewModel.Event) -> Unit,
    drawerState: DrawerState,
    tabsNavController: NavHostController,
    tabNavigator: TabNavigator,
    modifier: Modifier = Modifier
) {
    val rootNavController = rememberNavController()
    val rootNavigator = rememberRootNavigator(rootNavController)

    val koin = getKoin() // TODO через вью модель?
    val rootFeatureEntries: ImmutableList<RootFeatureEntry> = remember {
        koin.getAll<RootFeatureEntry>()
            .sortedBy { it::class.simpleName }
            .toImmutableList()
    }

    CompositionLocalProvider(
        LocalRootNavigator provides rootNavigator
    ) {
        NavHost(
            navController = rootNavController,
            startDestination = MainFlowRoute.Tabs,
            modifier = modifier.fillMaxSize()
        ) {
            composable<MainFlowRoute.Tabs> {
                CompositionLocalProvider(
                    LocalTabNavigator provides tabNavigator
                ) {
                    TabsView(
                        viewState = viewState,
                        onEvent = onEvent,
                        rootNavController = rootNavController,
                        drawerState = drawerState,
                        tabsNavController
                    )
                }
            }
            rootFeatureEntries.forEach { featureEntry ->
                featureEntry.registerGraph(navGraphBuilder = this)
            }
        }
    }
}

@NonRestartableComposable
@Composable
private fun rememberRootNavigator(
    rootNavController: NavHostController
): RootNavigator = remember(rootNavController) {
    object : RootNavigator {
        override fun goBack() {
            rootNavController.popBackStack()
        }

        override fun replace(route: RootRoute) {
            goBack()
            navigate(route)
        }

        override fun navigate(route: RootRoute) {
            rootNavController.navigate(route)
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
    tabsNavController: NavHostController) {
    val coroutineScope = rememberCoroutineScope()
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
                    rootNavController = rootNavController
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