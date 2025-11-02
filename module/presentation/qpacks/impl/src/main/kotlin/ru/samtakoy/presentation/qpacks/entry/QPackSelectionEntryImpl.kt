package ru.samtakoy.presentation.qpacks.entry

import androidx.compose.ui.text.AnnotatedString
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import ru.samtakoy.common.resources.Resources
import ru.samtakoy.presentation.navigation.MainTabFeatureEntry
import ru.samtakoy.presentation.navigation.MainTabRoute
import ru.samtakoy.presentation.qpacks.QPackSelectionRoute
import ru.samtakoy.presentation.qpacks.impl.R
import ru.samtakoy.presentation.utils.asA

// internal
class QPackSelectionEntryImpl(
    private val resources: Resources
) : MainTabFeatureEntry {

    override val route: MainTabRoute
        get() = QPackSelectionRoute

    override val routeTitle: AnnotatedString
        get() = resources.getString(R.string.feature_qpack_selection_title).asA()

    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        rootNavController: NavHostController,
        tabsNavController: NavHostController,
        onMainNavigator: () -> Unit
    ) {
        navGraphBuilder.composable<QPackSelectionRoute> {
            QPackSelectionEntry(
                navController = tabsNavController,
                onMainNavigator = onMainNavigator
            )
        }
    }
}