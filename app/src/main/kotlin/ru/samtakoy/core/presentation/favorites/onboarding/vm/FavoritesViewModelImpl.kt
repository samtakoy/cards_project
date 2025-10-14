package ru.samtakoy.core.presentation.favorites.onboarding.vm

import androidx.lifecycle.SavedStateHandle
import kotlinx.collections.immutable.toImmutableList
import ru.samtakoy.R
import ru.samtakoy.common.coroutines.ScopeProvider
import ru.samtakoy.common.resources.Resources
import ru.samtakoy.domain.favorites.FavoritesInteractor
import ru.samtakoy.presentation.base.viewmodel.BaseViewModelImpl
import ru.samtakoy.core.presentation.design_system.base.model.UiId
import ru.samtakoy.core.presentation.favorites.onboarding.mapper.FavoritesButtonsMapper
import ru.samtakoy.core.presentation.favorites.onboarding.vm.FavoritesViewModel.Action
import ru.samtakoy.domain.view.ViewHistoryInteractor

/**
 * TODO откуда при первом открытии фрагмента пауза?
 * */
internal class FavoritesViewModelImpl(
    private val viewHistoryInteractor: ViewHistoryInteractor,
    private val favoritesInteractor: FavoritesInteractor,
    private val favoritesButtonsMapper: FavoritesButtonsMapper,
    private val resources: Resources,
    savedStateHandle: SavedStateHandle,
    scopeProvider: ScopeProvider
) : BaseViewModelImpl<FavoritesViewModel.State, Action, FavoritesViewModel.Event>(
    scopeProvider,
    FavoritesViewModel.State(
        isLoaderVisible = false,
        buttons = favoritesButtonsMapper.mapInitial().toImmutableList()
    )
), FavoritesViewModel {

    init {
        launchCatching(onError = ::onSomeError) {
            viewState = viewState.copy(isLoaderVisible = true)
            val count = favoritesInteractor.getFavoriteCardsCount()
            viewState = viewState.copy(
                isLoaderVisible = false,
                buttons = favoritesButtonsMapper.map(favCount = count).toImmutableList()
            )
        }
    }

    override fun onEvent(event: FavoritesViewModel.Event) {
        when (event) {
            is FavoritesViewModel.Event.ButtonClick -> onButtonClick(event.id)
        }
    }

    private fun onButtonClick(id: UiId) {
        if (id !is FavoritesButtonsMapper.ButtonId) return
        when (id) {
            FavoritesButtonsMapper.ButtonId.AllFavoriteCards -> navigateToAllFavoriteCards()
            FavoritesButtonsMapper.ButtonId.FavoriteCardsFromPacks -> navigateToFavoriteCardsFromPacks()
        }
    }

    private fun navigateToAllFavoriteCards() {
        viewState = viewState.copy(isLoaderVisible = true)
        launchCatching(
            onError = ::onSomeError,
            onFinally = {
                viewState = viewState.copy(isLoaderVisible = false)
            }
        ) {
            val newView = viewHistoryInteractor.addNewViewItem(
                qPackId = 0,
                cardIds = favoritesInteractor.getAllFavoriteCardsIds()
            )
            sendAction(FavoritesViewModel.NavigationAction.ViewCardsFromCourse(viewItemId = newView.id))
        }
    }

    private fun navigateToFavoriteCardsFromPacks() {
        sendAction(FavoritesViewModel.NavigationAction.ViewQPacksWithFavs)
    }

    private fun onSomeError(t: Throwable) {
        sendAction(
            Action.ShowErrorMessage(resources.getString(R.string.common_err_message))
        )
    }
}