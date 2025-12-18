package ru.samtakoy.presentation.qpacks.screens.info.mapper

import kotlinx.collections.immutable.toImmutableList
import org.jetbrains.compose.resources.getString
import ru.samtakoy.presentation.core.design_system.base.model.AnyUiId
import ru.samtakoy.presentation.core.design_system.button.usual.MyButtonUiModel
import ru.samtakoy.presentation.core.design_system.dialogs.choice.MyChoiceDialogUiModel
import ru.samtakoy.presentation.core.design_system.radio.MyRadioItemUiModel
import ru.samtakoy.presentation.qpacks.screens.info.model.ChoiceButtonId
import ru.samtakoy.presentation.qpacks.screens.info.model.DialogId
import ru.samtakoy.presentation.utils.asA
import ru.samtakoy.resources.Res
import ru.samtakoy.resources.action_cancel
import ru.samtakoy.resources.action_ok
import ru.samtakoy.resources.qpack_btn_listen_cards
import ru.samtakoy.resources.qpack_btn_listen_cards_with_code
import ru.samtakoy.resources.qpack_btn_listen_questions
import ru.samtakoy.resources.qpack_viewing_play_types_title

internal interface QPackInfoPlayDialogMapper {
    suspend fun mapDialog(): MyChoiceDialogUiModel

    companion object {
        // айтемы выбора
        val IdItemCards = AnyUiId()
        val IdItemQuestions = AnyUiId()
        val IdItemCardsWithCode = AnyUiId()
    }
}

internal class QPackInfoPlayDialogMapperImpl : QPackInfoPlayDialogMapper {
    override suspend fun mapDialog(): MyChoiceDialogUiModel {
        return MyChoiceDialogUiModel(
            id = DialogId.PlayChoice,
            title = getString(Res.string.qpack_viewing_play_types_title).asA(),
            description = null,
            items = listOf<MyRadioItemUiModel>(
                MyRadioItemUiModel(
                    id = QPackInfoPlayDialogMapper.IdItemCards,
                    text = getString(Res.string.qpack_btn_listen_cards).asA(),
                    isSelected = true
                ),
                MyRadioItemUiModel(
                    id = QPackInfoPlayDialogMapper.IdItemQuestions,
                    text = getString(Res.string.qpack_btn_listen_questions).asA(),
                    isSelected = false
                ),
                MyRadioItemUiModel(
                    id = QPackInfoPlayDialogMapper.IdItemCardsWithCode,
                    text = getString(Res.string.qpack_btn_listen_cards_with_code).asA(),
                    isSelected = false
                ),
            ).toImmutableList(),
            firstButton = MyButtonUiModel(
                id = ChoiceButtonId.Cancel,
                text = getString(Res.string.action_cancel).asA(),
                isEnabled = true
            ),
            secondButton = MyButtonUiModel(
                id = ChoiceButtonId.Ok,
                text = getString(Res.string.action_ok).asA(),
                isEnabled = true
            )
        )
    }
}