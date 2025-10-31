package ru.samtakoy.presentation.themes.list.mapper

import kotlinx.collections.immutable.toImmutableList
import ru.samtakoy.common.resources.Resources
import ru.samtakoy.presentation.core.design_system.dropdown.DropDownMenuUiModel
import ru.samtakoy.presentation.core.design_system.dropdown.DropDownMenuUiModel.ItemType
import ru.samtakoy.presentation.core.design_system.dropdown.DropDownMenuUiModel.Menu
import ru.samtakoy.presentation.themes.impl.R
import ru.samtakoy.presentation.themes.list.model.ItemContextMenuId
import ru.samtakoy.presentation.themes.list.model.ThemeListMenuId
import ru.samtakoy.presentation.utils.asA
import ru.samtakoy.presentation.utils.asAnnotated

internal interface ThemeListMenuItemsMapper {
    fun mapShort(): DropDownMenuUiModel
    fun map(
        isExportAllMenuItemVisible: Boolean,
        isToBlankDbMenuItemVisible: Boolean,
    ): DropDownMenuUiModel
    fun mapThemeContextMenu(): DropDownMenuUiModel
    fun mapQPackContextMenu(): DropDownMenuUiModel
}

internal class ThemeListMenuItemsMapperImpl(
    private val resources: Resources
) : ThemeListMenuItemsMapper {

    override fun mapShort(): DropDownMenuUiModel {
        return DropDownMenuUiModel(
            menu = DropDownMenuUiModel.Menu(
                id = ThemeListMenuId.RootMenu,
                items = buildRootItemsShort().toImmutableList(),
            ),
            subMenus = emptyList<Menu>().toImmutableList()
        )
    }

    override fun map(
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

    private fun buildRootItemsShort(): List<ItemType> {
        return buildList {
            add(
                ItemType.Item(
                    id = ThemeListMenuId.Log,
                    title = resources.getString(
                        ru.samtakoy.common.utils.R.string.menu_item_log
                    ).asAnnotated()
                )
            )
            add(ItemType.Separator(ThemeListMenuId.DownSeparator))
            add(
                ItemType.Item(
                    id = ThemeListMenuId.Settings,
                    title = resources.getString(
                        ru.samtakoy.common.utils.R.string.menu_item_settings
                    ).asAnnotated()
                )
            )
        }
    }

    private fun buildRootItems(
        isExportAllMenuItemVisible: Boolean,
        isToBlankDbMenuItemVisible: Boolean
    ): List<ItemType> {
        return buildList {

            add(
                ItemType.Item(
                    id = ThemeListMenuId.ImportCards,
                    title = resources.getString(R.string.fragment_themes_menu_item_import_cards).asAnnotated()
                )
            )

            if (isToBlankDbMenuItemVisible) {
                add(
                    ItemType.Item(
                        id = ThemeListMenuId.ImportFromFolderAll,
                        title = resources.getString(R.string.menu_item_import_from_folder_all).asAnnotated()
                    )
                )
            } else {
                add(
                    ItemType.SubMenu(
                        id = ThemeListMenuId.FromFolderSubMenu,
                        title = resources.getString(R.string.menu_item_from_folder).asAnnotated()
                    )
                )
            }

            if (isToBlankDbMenuItemVisible) {
                add(
                    ItemType.Item(
                        id = ThemeListMenuId.ImportFromZipAll,
                        title = resources.getString(R.string.menu_item_import_from_zip_all).asAnnotated()
                    )
                )
            } else {
                add(
                    ItemType.SubMenu(
                        id = ThemeListMenuId.FromZipSubMenu,
                        title = resources.getString(R.string.menu_item_from_zip).asAnnotated()
                    )
                )
            }

            add(
                ItemType.Item(
                    id = ThemeListMenuId.OnlineImportCards,
                    title = resources.getString(R.string.fragment_themes_menu_item_remote_import_cards)
                        .asAnnotated()
                )
            )

            if (isExportAllMenuItemVisible) {
                add(
                    ItemType.Item(
                        id = ThemeListMenuId.ExportAllToDir,
                        title = resources.getString(R.string.fragment_themes_menu_item_export_all_to)
                            .asAnnotated()
                    )
                )

                add(
                    ItemType.Item(
                        id = ThemeListMenuId.ExportAllToEmail,
                        title = resources.getString(R.string.fragment_themes_menu_item_export_all_to_email)
                            .asAnnotated()
                    )
                )
            }

            add(
                ItemType.Item(
                    id = ThemeListMenuId.Log,
                    title = resources.getString(ru.samtakoy.common.utils.R.string.menu_item_log).asAnnotated()
                )
            )

            add(ItemType.Separator(ThemeListMenuId.DownSeparator))

            add(
                ItemType.Item(
                    id = ThemeListMenuId.Settings,
                    title = resources.getString(
                        ru.samtakoy.common.utils.R.string.menu_item_settings
                    ).asAnnotated()
                )
            )

        }
    }

    private fun buildSubMenus(): List<DropDownMenuUiModel.Menu> {
        return listOf(
            buildFromFolderSubMenu(),
            buildFromZipSubMenu()
        )
    }

    private fun buildFromFolderSubMenu(): DropDownMenuUiModel.Menu {
        return DropDownMenuUiModel.Menu(
            id = ThemeListMenuId.FromFolderSubMenu,
            items = listOf<ItemType>(
                ItemType.Item(
                    id = ThemeListMenuId.FromFolderImportNew,
                    title = resources.getString(R.string.menu_item_import_new).asAnnotated()
                ),
                ItemType.Item(
                    id = ThemeListMenuId.FromFolderUpdateExists,
                    title = resources.getString(R.string.menu_item_update_exists).asAnnotated()
                ),
                ItemType.Item(
                    id = ThemeListMenuId.FromFolderImportAsNew,
                    title = resources.getString(R.string.menu_item_import_as_new).asAnnotated()
                )
            ).toImmutableList()
        )
    }

    private fun buildFromZipSubMenu(): DropDownMenuUiModel.Menu {
        return DropDownMenuUiModel.Menu(
            id = ThemeListMenuId.FromZipSubMenu,
            items = listOf<ItemType>(
                ItemType.Item(
                    id = ThemeListMenuId.FromZipImportNew,
                    title = resources.getString(R.string.menu_item_import_new).asAnnotated()
                ),
                ItemType.Item(
                    id = ThemeListMenuId.FromZipUpdateExists,
                    title = resources.getString(R.string.menu_item_update_exists).asAnnotated()
                ),
                ItemType.Item(
                    id = ThemeListMenuId.FromZipImportAsNew,
                    title = resources.getString(R.string.menu_item_import_as_new).asAnnotated()
                )
            ).toImmutableList()
        )
    }

    override fun mapThemeContextMenu(): DropDownMenuUiModel {
        return DropDownMenuUiModel(
            menu = DropDownMenuUiModel.Menu(
                id = ItemContextMenuId.ThemeMenu,
                items = listOf<ItemType>(
                    ItemType.Item(
                        id = ItemContextMenuId.DeleteTheme,
                        title = resources.getString(R.string.fragment_themes_menu_item_delete_theme).asA()
                    )
                ).toImmutableList()
            ),
            subMenus = null
        )
    }

    override fun mapQPackContextMenu(): DropDownMenuUiModel {
        return DropDownMenuUiModel(
            menu = DropDownMenuUiModel.Menu(
                id = ItemContextMenuId.QPackMenu,
                items = listOf<ItemType>(
                    ItemType.Item(
                        id = ItemContextMenuId.ExportQPackToEmail,
                        title = resources.getString(R.string.fragment_themes_menu_item_send_cards).asA()
                    )
                ).toImmutableList()
            ),
            subMenus = null
        )
    }
}