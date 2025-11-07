package ru.samtakoy.core.presentation.favorites.onboarding.mapper

import kotlinx.parcelize.Parcelize
import ru.samtakoy.R
import ru.samtakoy.common.resources.Resources
import ru.samtakoy.presentation.core.design_system.base.model.UiId
import ru.samtakoy.presentation.core.design_system.button.usual.MyButtonUiModel
import ru.samtakoy.presentation.utils.asAnnotated

interface FavoritesButtonsMapper {
    fun mapInitial(): List<MyButtonUiModel>
    fun map(favCount: Int): List<MyButtonUiModel>

    @Parcelize
    enum class ButtonId : UiId {
        AllFavoriteCards,
        FavoriteCardsFromPacks
    }
}

internal class FavoritesButtonsMapperImpl(
    private val resources: Resources
) : FavoritesButtonsMapper {
    override fun mapInitial(): List<MyButtonUiModel> {
        return listOf(
            MyButtonUiModel(
                id = FavoritesButtonsMapper.ButtonId.AllFavoriteCards,
                text = resources.getString(R.string.favorites_view_all_cards_button).asAnnotated()
            ),
            MyButtonUiModel(
                id = FavoritesButtonsMapper.ButtonId.FavoriteCardsFromPacks,
                text = resources.getString(R.string.favorites_view_from_packs_button).asAnnotated()
            )
        )
    }

    override fun map(favCount: Int): List<MyButtonUiModel> {
        return listOf(
            MyButtonUiModel(
                id = FavoritesButtonsMapper.ButtonId.AllFavoriteCards,
                text = resources
                    .getString(R.string.favorites_view_all_cards_button_arg, favCount)
                    .asAnnotated()
            ),
            MyButtonUiModel(
                id = FavoritesButtonsMapper.ButtonId.FavoriteCardsFromPacks,
                text = resources.getString(R.string.favorites_view_from_packs_button).asAnnotated()
            )
        )
    }
}