package ru.samtakoy.presentation.themes.list.mapper

import kotlinx.collections.immutable.toImmutableList
import org.jetbrains.compose.resources.getString
import ru.samtakoy.presentation.core.design_system.dropdown.DropDownMenuUiModel
import ru.samtakoy.presentation.core.design_system.dropdown.DropDownMenuUiModel.ItemType
import ru.samtakoy.presentation.core.design_system.dropdown.DropDownMenuUiModel.Menu
import ru.samtakoy.presentation.themes.list.model.ItemContextMenuId
import ru.samtakoy.presentation.themes.list.model.ThemeListMenuId
import ru.samtakoy.presentation.utils.asA
import ru.samtakoy.presentation.utils.asAnnotated
import ru.samtakoy.resources.Res
import ru.samtakoy.resources.fragment_themes_menu_item_delete_theme
import ru.samtakoy.resources.fragment_themes_menu_item_export_all_to
import ru.samtakoy.resources.fragment_themes_menu_item_export_all_to_email
import ru.samtakoy.resources.fragment_themes_menu_item_import_cards
import ru.samtakoy.resources.fragment_themes_menu_item_remote_import_cards
import ru.samtakoy.resources.fragment_themes_menu_item_send_cards
import ru.samtakoy.resources.menu_item_from_folder
import ru.samtakoy.resources.menu_item_from_zip
import ru.samtakoy.resources.menu_item_import_as_new
import ru.samtakoy.resources.menu_item_import_from_folder_all
import ru.samtakoy.resources.menu_item_import_from_zip_all
import ru.samtakoy.resources.menu_item_import_new
import ru.samtakoy.resources.menu_item_log
import ru.samtakoy.resources.menu_item_settings
import ru.samtakoy.resources.menu_item_update_exists

internal interface ThemeListMenuItemsMapper {
    suspend fun mapShort(): DropDownMenuUiModel
    suspend fun map(
        isExportAllMenuItemVisible: Boolean,
        isToBlankDbMenuItemVisible: Boolean,
    ): DropDownMenuUiModel
    suspend fun mapThemeContextMenu(): DropDownMenuUiModel
    suspend fun mapQPackContextMenu(): DropDownMenuUiModel
}

internal class ThemeListMenuItemsMapperImpl : ThemeListMenuItemsMapper {

    override suspend fun mapShort(): DropDownMenuUiModel {
        return DropDownMenuUiModel(
            menu = DropDownMenuUiModel.Menu(
                id = ThemeListMenuId.RootMenu,
                items = buildRootItemsShort().toImmutableList(),
            ),
            subMenus = emptyList<Menu>().toImmutableList()
        )
    }

    override suspend fun map(
        isExportAllMenuItemVisible: Boolean,
        isToBlankDbMenuItemVisible: Boolean,
    ): DropDownMenuUiModel {
        return DropDownMenuUiModel(
            menu = DropDownMenuUiModel.Menu(
                id = ThemeListMenuId.RootMenu,
                items = buildRootItems(
                    isExportAllMenuItemVisible = isExportAllMenuItemVisible,
                    isToBlankDbMenuItemVisible = isToBlankDbMenuItemVisible
                ).toImmutableList(),
            ),
            subMenus = buildSubMenus().toImmutableList()
        )
    }

    private suspend fun buildRootItemsShort(): List<ItemType> {
        return buildList {
            add(
                ItemType.Item(
                    id = ThemeListMenuId.Log,
                    title = getString(
                        Res.string.menu_item_log
                    ).asAnnotated()
                )
            )
            add(ItemType.Separator(ThemeListMenuId.DownSeparator))
            add(
                ItemType.Item(
                    id = ThemeListMenuId.Settings,
                    title = getString(
                        Res.string.menu_item_settings
                    ).asAnnotated()
                )
            )
        }
    }

    private suspend fun buildRootItems(
        isExportAllMenuItemVisible: Boolean,
        isToBlankDbMenuItemVisible: Boolean
    ): List<ItemType> {
        return buildList {

            add(
                ItemType.Item(
                    id = ThemeListMenuId.ImportCards,
                    title = getString(Res.string.fragment_themes_menu_item_import_cards).asAnnotated()
                )
            )

            if (isToBlankDbMenuItemVisible) {
                add(
                    ItemType.Item(
                        id = ThemeListMenuId.ImportFromFolderAll,
                        title = getString(Res.string.menu_item_import_from_folder_all).asAnnotated()
                    )
                )
            } else {
                add(
                    ItemType.SubMenu(
                        id = ThemeListMenuId.FromFolderSubMenu,
                        title = getString(Res.string.menu_item_from_folder).asAnnotated()
                    )
                )
            }

            if (isToBlankDbMenuItemVisible) {
                add(
                    ItemType.Item(
                        id = ThemeListMenuId.ImportFromZipAll,
                        title = getString(Res.string.menu_item_import_from_zip_all).asAnnotated()
                    )
                )
            } else {
                add(
                    ItemType.SubMenu(
                        id = ThemeListMenuId.FromZipSubMenu,
                        title = getString(Res.string.menu_item_from_zip).asAnnotated()
                    )
                )
            }

            add(
                ItemType.Item(
                    id = ThemeListMenuId.OnlineImportCards,
                    title = getString(Res.string.fragment_themes_menu_item_remote_import_cards)
                        .asAnnotated()
                )
            )

            if (isExportAllMenuItemVisible) {
                add(
                    ItemType.Item(
                        id = ThemeListMenuId.ExportAllToDir,
                        title = getString(Res.string.fragment_themes_menu_item_export_all_to)
                            .asAnnotated()
                    )
                )

                add(
                    ItemType.Item(
                        id = ThemeListMenuId.ExportAllToEmail,
                        title = getString(Res.string.fragment_themes_menu_item_export_all_to_email)
                            .asAnnotated()
                    )
                )
            }

            add(
                ItemType.Item(
                    id = ThemeListMenuId.Log,
                    title = getString(Res.string.menu_item_log).asAnnotated()
                )
            )

            add(ItemType.Separator(ThemeListMenuId.DownSeparator))

            add(
                ItemType.Item(
                    id = ThemeListMenuId.Settings,
                    title = getString(
                        Res.string.menu_item_settings
                    ).asAnnotated()
                )
            )

        }
    }

    private suspend fun buildSubMenus(): List<DropDownMenuUiModel.Menu> {
        return listOf(
            buildFromFolderSubMenu(),
            buildFromZipSubMenu()
        )
    }

    private suspend fun buildFromFolderSubMenu(): DropDownMenuUiModel.Menu {
        return DropDownMenuUiModel.Menu(
            id = ThemeListMenuId.FromFolderSubMenu,
            items = listOf<ItemType>(
                ItemType.Item(
                    id = ThemeListMenuId.FromFolderImportNew,
                    title = getString(Res.string.menu_item_import_new).asAnnotated()
                ),
                ItemType.Item(
                    id = ThemeListMenuId.FromFolderUpdateExists,
                    title = getString(Res.string.menu_item_update_exists).asAnnotated()
                ),
                ItemType.Item(
                    id = ThemeListMenuId.FromFolderImportAsNew,
                    title = getString(Res.string.menu_item_import_as_new).asAnnotated()
                )
            ).toImmutableList()
        )
    }

    private suspend fun buildFromZipSubMenu(): DropDownMenuUiModel.Menu {
        return DropDownMenuUiModel.Menu(
            id = ThemeListMenuId.FromZipSubMenu,
            items = listOf<ItemType>(
                ItemType.Item(
                    id = ThemeListMenuId.FromZipImportNew,
                    title = getString(Res.string.menu_item_import_new).asAnnotated()
                ),
                ItemType.Item(
                    id = ThemeListMenuId.FromZipUpdateExists,
                    title = getString(Res.string.menu_item_update_exists).asAnnotated()
                ),
                ItemType.Item(
                    id = ThemeListMenuId.FromZipImportAsNew,
                    title = getString(Res.string.menu_item_import_as_new).asAnnotated()
                )
            ).toImmutableList()
        )
    }

    override suspend fun mapThemeContextMenu(): DropDownMenuUiModel {
        return DropDownMenuUiModel(
            menu = DropDownMenuUiModel.Menu(
                id = ItemContextMenuId.ThemeMenu,
                items = listOf<ItemType>(
                    ItemType.Item(
                        id = ItemContextMenuId.DeleteTheme,
                        title = getString(Res.string.fragment_themes_menu_item_delete_theme).asA()
                    )
                ).toImmutableList()
            ),
            subMenus = null
        )
    }

    override suspend fun mapQPackContextMenu(): DropDownMenuUiModel {
        return DropDownMenuUiModel(
            menu = DropDownMenuUiModel.Menu(
                id = ItemContextMenuId.QPackMenu,
                items = listOf<ItemType>(
                    ItemType.Item(
                        id = ItemContextMenuId.ExportQPackToEmail,
                        title = getString(Res.string.fragment_themes_menu_item_send_cards).asA()
                    )
                ).toImmutableList()
            ),
            subMenus = null
        )
    }
}