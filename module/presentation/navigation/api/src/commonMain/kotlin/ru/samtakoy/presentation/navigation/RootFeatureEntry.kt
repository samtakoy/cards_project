package ru.samtakoy.presentation.navigation

import androidx.compose.runtime.Immutable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController

@Immutable
interface RootFeatureEntry {
    fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        rootNavController: NavHostController
    )
}