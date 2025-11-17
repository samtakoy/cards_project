package ru.samtakoy.presentation.navigation

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController

@Immutable
interface RootFeatureEntry {
    @Stable
    fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        rootNavController: NavHostController
    )
}