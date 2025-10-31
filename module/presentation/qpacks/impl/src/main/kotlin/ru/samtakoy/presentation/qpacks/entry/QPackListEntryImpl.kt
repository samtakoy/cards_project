package ru.samtakoy.presentation.themes.entry

import androidx.compose.ui.text.AnnotatedString
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import ru.samtakoy.common.resources.Resources
import ru.samtakoy.presentation.navigation.MainFeatureEntry
import ru.samtakoy.presentation.navigation.MainRoute
import ru.samtakoy.presentation.qpacks.QPackListRoute
import ru.samtakoy.presentation.qpacks.impl.R
import ru.samtakoy.presentation.utils.asA

// internal
class QPackListEntryImpl(
    private val resources: Resources
) : MainFeatureEntry {

    override val route: MainRoute
        get() = QPackListRoute

    override val routeTitle: AnnotatedString
        get() = resources.getString(R.string.feature_qpack_title).asA()

    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        rootNavController: NavHostController,
        tabsNavController: NavHostController,
        onMainNavigator: () -> Unit
    ) {
        navGraphBuilder.composable<QPackListRoute> {
            QPackListEntry(
                navController = tabsNavController,
                onMainNavigator = onMainNavigator
            )
        }
    }
}