package ru.samtakoy.presentation.core.design_system.dropdown

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import ru.samtakoy.presentation.base.model.UiId

@Composable
fun MyDropDownMenuBox(
    menu: DropDownMenuUiModel,
    onMenuItemClick: (item: DropDownMenuUiModel.ItemType.Item) -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.(onOpenEvent: () -> Unit) -> Unit
) {
    var openedMenuId: UiId? by remember { mutableStateOf(null) }
    val currentOpenedMenu: DropDownMenuUiModel.Menu? by remember(menu.menu) {
        derivedStateOf {
            if (openedMenuId == null) {
                null
            } else if (openedMenuId == menu.menu.id) {
                menu.menu
            } else {
                menu.subMenus?.find { it.id == openedMenuId }
            }
        }
    }
    val isMenuExpanded = currentOpenedMenu != null

    Box(modifier = modifier) {
        content {
            openedMenuId = menu.menu.id
        }
        DropdownMenu(
            expanded = isMenuExpanded,
            onDismissRequest = { openedMenuId = null }
        ) {
            MyDropDownMenuItems(
                menu = currentOpenedMenu,
                onItemClick = { item: DropDownMenuUiModel.ItemType.Item ->
                    openedMenuId = null
                    onMenuItemClick(item)
                },
                onSubMenuOpen = { subMenuId ->
                    openedMenuId = subMenuId
                }
            )
        }
    }
}

@Composable
private fun ColumnScope.MyDropDownMenuItems(
    menu: DropDownMenuUiModel.Menu?,
    onItemClick: (item: DropDownMenuUiModel.ItemType.Item) -> Unit,
    onSubMenuOpen: (menuId: UiId) -> Unit
) {
    var lastNonNullMenu by remember { mutableStateOf<DropDownMenuUiModel.Menu?>(null) }
    val lastMenu = remember(menu) {
        if (menu != null) {
            lastNonNullMenu = menu
        }
        lastNonNullMenu
    }
    lastMenu?.items?.forEach { menuItem ->
        key(menuItem.id) {
            DropdownMenuItem(
                text = { MenuItemContent(menuItem) },
                onClick = {
                    when (menuItem) {
                        is DropDownMenuUiModel.ItemType.Item -> onItemClick(menuItem)
                        is DropDownMenuUiModel.ItemType.Separator -> Unit
                        is DropDownMenuUiModel.ItemType.SubMenu -> onSubMenuOpen(menuItem.id)
                    }
                }
            )
        }
    }
}

@Composable
private fun MenuItemContent(
    menuItem: DropDownMenuUiModel.ItemType,
    modifier: Modifier = Modifier
) {
    when (menuItem) {
        is DropDownMenuUiModel.ItemType.Item -> {
            Text(
                text = menuItem.title,
                modifier = modifier,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.labelMedium,
            )
        }
        is DropDownMenuUiModel.ItemType.Separator -> {
            HorizontalDivider()
        }
        is DropDownMenuUiModel.ItemType.SubMenu -> {
            Text(
                text = menuItem.title,
                modifier = modifier,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.labelMedium,
            )
        }
    }
}