package ru.samtakoy.presentation.themes.list.mapper

import kotlinx.collections.immutable.toImmutableList
import org.jetbrains.compose.resources.getString
import ru.samtakoy.common.utils.DateUtils
import ru.samtakoy.common.utils.coroutines.SuspendLazy
import ru.samtakoy.domain.qpack.QPack
import ru.samtakoy.domain.theme.Theme
import ru.samtakoy.presentation.base.model.AnyUiId
import ru.samtakoy.presentation.base.model.LongUiId
import ru.samtakoy.presentation.core.design_system.button.usual.MyButtonUiModel
import ru.samtakoy.presentation.core.design_system.dialogs.alert.MyAlertDialogUiModel
import ru.samtakoy.presentation.core.design_system.dialogs.inputtext.MyInputTextDialogUiModel
import ru.samtakoy.presentation.themes.list.model.ThemeUiItem
import ru.samtakoy.presentation.themes.list.vm.ThemeListViewModel
import ru.samtakoy.presentation.utils.asA
import ru.samtakoy.presentation.utils.asAnnotated
import ru.samtakoy.resources.Res
import ru.samtakoy.resources.action_cancel
import ru.samtakoy.resources.action_ok
import ru.samtakoy.resources.feature_themes_list_import_dialog_title
import ru.samtakoy.resources.feature_themes_list_import_url_button
import ru.samtakoy.resources.feature_themes_list_import_url_desc
import ru.samtakoy.resources.fragment_dialog_theme_add_title
import ru.samtakoy.resources.theme_list_screen_notifications_alert_desc
import ru.samtakoy.resources.theme_list_screen_notifications_alert_title
import kotlin.time.ExperimentalTime

internal interface ThemeUiItemMapper {
    fun mapThemes(themes: List<Theme>): List<ThemeUiItem.Theme>
    fun mapQPacks(qPacks: List<QPack>): List<ThemeUiItem.QPack>
    suspend fun mapNotificationsAlertDialog(): MyAlertDialogUiModel
    suspend fun mapContent(
        parentTheme: Theme?,
        childThemes: List<Theme>,
        childQPacks: List<QPack>
    ): ThemeListViewModel.State.Content
    suspend fun mapNewThemeNameInputDialog(): MyInputTextDialogUiModel
    suspend fun mapExampleUrlInputDialog(): MyInputTextDialogUiModel

    companion object {
        val NotificationsAlertDialogId = AnyUiId()
        val NewThemeNameInputDialogId = AnyUiId()
        val ExampleUrlInputDialogId = AnyUiId()
        val OkBtnId = AnyUiId()
        val CancelBtnId = AnyUiId()
        val ImportExampleBtnId = AnyUiId()
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

    override suspend fun mapContent(
        parentTheme: Theme?,
        childThemes: List<Theme>,
        childQPacks: List<QPack>
    ): ThemeListViewModel.State.Content {
        val items = (mapThemes(childThemes) + mapQPacks(childQPacks)).toImmutableList()
        return if (parentTheme == null && items.isEmpty()) {
            ThemeListViewModel.State.Content.Empty(
                actionButton = importExampleButton.getValue(),
                description = getString(Res.string.feature_themes_list_import_url_desc).asA()
            )
        } else {
            ThemeListViewModel.State.Content.Items(items = items)
        }
    }

    override suspend fun mapNewThemeNameInputDialog(): MyInputTextDialogUiModel {
        return MyInputTextDialogUiModel(
            id = ThemeUiItemMapper.NewThemeNameInputDialogId,
            title = getString(Res.string.fragment_dialog_theme_add_title).asAnnotated(),
            description = null,
            inputHint = null,
            initialText = "",
            okButton = okButton.getValue()
        )
    }

    override suspend fun mapExampleUrlInputDialog(): MyInputTextDialogUiModel {
        return MyInputTextDialogUiModel(
            id = ThemeUiItemMapper.ExampleUrlInputDialogId,
            title = getString(Res.string.feature_themes_list_import_dialog_title).asAnnotated(),
            description = null,
            inputHint = null,
            initialText = DEFAULT_EXAMPLE_ZIP_URL,
            okButton = okButton.getValue()
        )
    }

    private fun QPack.getCreationDateAsString(): String {
        @OptIn(ExperimentalTime::class)
        return DateUtils.formatToString(creationDate)
    }

    private val importExampleButton = SuspendLazy {
        MyButtonUiModel(
            id = ThemeUiItemMapper.ImportExampleBtnId,
            text = getString(Res.string.feature_themes_list_import_url_button).asAnnotated()
        )
    }

    private val okButton = SuspendLazy {
        MyButtonUiModel(
            id = LongUiId(0L),
            text = getString(Res.string.action_ok).asAnnotated(),
            isEnabled = true
        )
    }

    companion object {
        private const val DEFAULT_EXAMPLE_ZIP_URL = "https://raw.githubusercontent.com/samtakoy/data/master/cards_example.zip"
    }
}