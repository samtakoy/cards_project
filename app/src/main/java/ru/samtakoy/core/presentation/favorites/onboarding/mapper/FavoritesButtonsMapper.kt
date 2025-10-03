package ru.samtakoy.core.presentation.favorites.onboarding.mapper

import kotlinx.parcelize.Parcelize
import ru.samtakoy.R
import ru.samtakoy.core.app.some.Resources
import ru.samtakoy.core.app.utils.asAnnotated
import ru.samtakoy.core.presentation.design_system.base.model.UiId
import ru.samtakoy.core.presentation.design_system.button.MyButtonModel
import javax.inject.Inject

interface FavoritesButtonsMapper {
    fun mapInitial(): List<MyButtonModel>
    fun map(favCount: Int): List<MyButtonModel>

    @Parcelize
    enum class ButtonId : UiId {
        AllFavoriteCards,
        FavoriteCardsFromPacks
    }
}

internal class FavoritesButtonsMapperImpl @Inject constructor(
    private val resources: Resources
) : FavoritesButtonsMapper {
    override fun mapInitial(): List<MyButtonModel> {
        return listOf(
            MyButtonModel(
                id = FavoritesButtonsMapper.ButtonId.AllFavoriteCards,
                text = resources.getString(R.string.favorites_view_all_cards_button).asAnnotated()
            ),
            MyButtonModel(
                id = FavoritesButtonsMapper.ButtonId.FavoriteCardsFromPacks,
                text = resources.getString(R.string.favorites_view_from_packs_button).asAnnotated()
            )
        )
    }

    override fun map(favCount: Int): List<MyButtonModel> {
        return listOf(
            MyButtonModel(
                id = FavoritesButtonsMapper.ButtonId.AllFavoriteCards,
                text = resources
                    .getString(R.string.favorites_view_all_cards_button_arg, favCount)
                    .asAnnotated()
            ),
            MyButtonModel(
                id = FavoritesButtonsMapper.ButtonId.FavoriteCardsFromPacks,
                text = resources.getString(R.string.favorites_view_from_packs_button).asAnnotated()
            )
        )
    }
}