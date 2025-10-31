package ru.samtakoy.presentation.themes.list.mapper

import kotlinx.collections.immutable.toImmutableList
import ru.samtakoy.common.resources.Resources
import ru.samtakoy.common.utils.DateUtils.DATE_FORMAT
import ru.samtakoy.domain.qpack.QPack
import ru.samtakoy.domain.theme.Theme
import ru.samtakoy.presentation.core.design_system.base.model.AnyUiId
import ru.samtakoy.presentation.core.design_system.base.model.LongUiId
import ru.samtakoy.presentation.core.design_system.button.MyButtonUiModel
import ru.samtakoy.presentation.core.design_system.dialogs.MyAlertDialogUiModel
import ru.samtakoy.presentation.themes.impl.R
import ru.samtakoy.presentation.themes.list.model.ThemeUiItem
import ru.samtakoy.presentation.utils.asA
import ru.samtakoy.presentation.utils.asAnnotated

internal interface ThemeUiItemMapper {
    fun mapThemes(themes: List<Theme>): List<ThemeUiItem.Theme>
    fun mapQPacks(qPacks: List<QPack>): List<ThemeUiItem.QPack>
    fun mapNotificationsAlertDialog(): MyAlertDialogUiModel

    companion object {
        val NotificationsAlertDialogId = AnyUiId()
        val OkBtnId = AnyUiId()
        val CancelBtnId = AnyUiId()
    }
}

internal class ThemeUiItemMapperImpl(
    private val resources: Resources
) : ThemeUiItemMapper {
    override fun mapThemes(themes: List<Theme>): List<ThemeUiItem.Theme> {
        return themes.map {
            ThemeUiItem.Theme(
                composeKey = "t${it.id}",
                id = LongUiId(it.id),
                title = it.title.asAnnotated()
            )
        }
    }

    override fun mapQPacks(qPacks: List<QPack>): List<ThemeUiItem.QPack> {
        return qPacks.map {
            ThemeUiItem.QPack(
                composeKey = "q${it.id}",
                id = LongUiId(it.id),
                title = it.title.asAnnotated(),
                creationDate = it.getCreationDateAsString().asAnnotated()
            )
        }
    }

    override fun mapNotificationsAlertDialog(): MyAlertDialogUiModel {
        return MyAlertDialogUiModel(
            id = ThemeUiItemMapper.NotificationsAlertDialogId,
            title = resources.getString(R.string.theme_list_screen_notifications_alert_title).asA(),
            description = resources.getString(R.string.theme_list_screen_notifications_alert_desc).asA(),
            buttons = listOf<MyButtonUiModel>(
                MyButtonUiModel(
                    id = ThemeUiItemMapper.OkBtnId,
                    text = resources.getString(ru.samtakoy.common.utils.R.string.action_ok).asA(),
                    isEnabled = true
                ),
                MyButtonUiModel(
                    id = ThemeUiItemMapper.CancelBtnId,
                    text = resources.getString(ru.samtakoy.common.utils.R.string.action_cancel).asA(),
                    isEnabled = true
                )
            ).toImmutableList()
        )
    }

    private fun QPack.getCreationDateAsString(): String {
        return DATE_FORMAT.format(creationDate)
    }
}