package ru.samtakoy.presentation.themes.list.mapper

import kotlinx.collections.immutable.toImmutableList
import org.jetbrains.compose.resources.getString
import ru.samtakoy.common.utils.DateUtils
import ru.samtakoy.domain.qpack.QPack
import ru.samtakoy.domain.theme.Theme
import ru.samtakoy.presentation.core.design_system.base.model.AnyUiId
import ru.samtakoy.presentation.core.design_system.base.model.LongUiId
import ru.samtakoy.presentation.core.design_system.button.usual.MyButtonUiModel
import ru.samtakoy.presentation.core.design_system.dialogs.alert.MyAlertDialogUiModel
import ru.samtakoy.presentation.themes.list.model.ThemeUiItem
import ru.samtakoy.presentation.utils.asA
import ru.samtakoy.presentation.utils.asAnnotated
import ru.samtakoy.resources.Res
import ru.samtakoy.resources.action_cancel
import ru.samtakoy.resources.action_ok
import ru.samtakoy.resources.theme_list_screen_notifications_alert_desc
import ru.samtakoy.resources.theme_list_screen_notifications_alert_title
import kotlin.time.ExperimentalTime

internal interface ThemeUiItemMapper {
    fun mapThemes(themes: List<Theme>): List<ThemeUiItem.Theme>
    fun mapQPacks(qPacks: List<QPack>): List<ThemeUiItem.QPack>
    suspend fun mapNotificationsAlertDialog(): MyAlertDialogUiModel

    companion object {
        val NotificationsAlertDialogId = AnyUiId()
        val OkBtnId = AnyUiId()
        val CancelBtnId = AnyUiId()
    }
}

internal class ThemeUiItemMapperImpl : ThemeUiItemMapper {
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
                title = it.title.trim { it.isWhitespace() }.asAnnotated(),
                creationDate = it.getCreationDateAsString().asAnnotated(),
                viewCount = it.viewCount.toString().asA()
            )
        }
    }

    override suspend fun mapNotificationsAlertDialog(): MyAlertDialogUiModel {
        return MyAlertDialogUiModel(
            id = ThemeUiItemMapper.NotificationsAlertDialogId,
            title = getString(Res.string.theme_list_screen_notifications_alert_title).asA(),
            description = getString(Res.string.theme_list_screen_notifications_alert_desc).asA(),
            buttons = listOf<MyButtonUiModel>(
                MyButtonUiModel(
                    id = ThemeUiItemMapper.OkBtnId,
                    text = getString(Res.string.action_ok).asA(),
                    isEnabled = true
                ),
                MyButtonUiModel(
                    id = ThemeUiItemMapper.CancelBtnId,
                    text = getString(Res.string.action_cancel).asA(),
                    isEnabled = true
                )
            ).toImmutableList()
        )
    }

    private fun QPack.getCreationDateAsString(): String {
        @OptIn(ExperimentalTime::class)
        return DateUtils.formatToString(creationDate)
    }
}