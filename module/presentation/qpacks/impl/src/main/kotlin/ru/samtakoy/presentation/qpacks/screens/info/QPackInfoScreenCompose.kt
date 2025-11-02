package ru.samtakoy.presentation.qpacks.screens.info

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.collections.immutable.toImmutableList
import org.koin.android.ext.koin.androidContext
import org.koin.core.KoinApplication
import ru.samtakoy.presentation.base.observeActionsWithLifecycle
import ru.samtakoy.presentation.core.design_system.base.UiOffsets
import ru.samtakoy.presentation.core.design_system.base.model.AnyUiId
import ru.samtakoy.presentation.core.design_system.base.theme.MyTheme
import ru.samtakoy.presentation.core.design_system.button.MyButton
import ru.samtakoy.presentation.core.design_system.dialogs.choice.MyChoiceDialogUiModel
import ru.samtakoy.presentation.core.design_system.dialogs.choice.MyChoiceDialogView
import ru.samtakoy.presentation.core.design_system.dropdown.MyDropDownMenuBox
import ru.samtakoy.presentation.core.design_system.dropdown.getEmptyMenu
import ru.samtakoy.presentation.core.design_system.scaffold.MySimpleScreenScaffold
import ru.samtakoy.presentation.core.design_system.selectable_item.MySelectableItem
import ru.samtakoy.presentation.core.design_system.selectable_item.MySelectableItemModel
import ru.samtakoy.presentation.core.design_system.toolbar.ToolbarTitleView
import ru.samtakoy.presentation.qpacks.di.qPackPresentationModule
import ru.samtakoy.presentation.qpacks.impl.R
import ru.samtakoy.presentation.qpacks.screens.info.mapper.QPackInfoButtonsMapper
import ru.samtakoy.presentation.qpacks.screens.info.mapper.QPackInfoButtonsMapper.Uncompleted
import ru.samtakoy.presentation.qpacks.screens.info.vm.QPackInfoViewModel
import ru.samtakoy.presentation.qpacks.screens.info.vm.QPackInfoViewModel.Event
import ru.samtakoy.presentation.qpacks.screens.info.vm.QPackInfoViewModel.NavigationAction
import ru.samtakoy.presentation.qpacks.screens.info.vm.QPackInfoViewModel.State
import ru.samtakoy.presentation.utils.asA

@Composable
internal fun QPackInfoScreen(
    viewModel: QPackInfoViewModel,
    onNavigationAction: (NavigationAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val snackbarHostState by remember { mutableStateOf(SnackbarHostState()) }
    val viewState by viewModel.getViewStateAsFlow().collectAsStateWithLifecycle()
    val choiceDialogState: MutableState<MyChoiceDialogUiModel?> = remember { mutableStateOf(null) }

    QPackInfoScreenInternal(
        viewState = viewState,
        onEvent = viewModel::onEvent,
        snackbarHostState = snackbarHostState,
        modifier = modifier
    )

    HandleActions(
        viewModel = viewModel,
        onEvent = viewModel::onEvent,
        onNavigationAction = onNavigationAction,
        snackbarHostState = snackbarHostState,
        choiceDialogState = choiceDialogState,
    )

    ScreenDialogs(
        choiceDialogState = choiceDialogState,
        onEvent = viewModel::onEvent
    )
}

@Composable
private fun ScreenDialogs(
    choiceDialogState: MutableState<MyChoiceDialogUiModel?>,
    onEvent: (event: Event) -> Unit
) {
    MyChoiceDialogView(
        dialogState = choiceDialogState,
        onButtonClick = { dialogId, selectedItemId ->
            selectedItemId?.let {
                onEvent(Event.ViewTypeCommit(selectedItemId))
            }
        }
    )
}

@Composable
private fun HandleActions(
    viewModel: QPackInfoViewModel,
    onEvent: (event: Event) -> Unit,
    onNavigationAction: (NavigationAction) -> Unit,
    snackbarHostState: SnackbarHostState,
    choiceDialogState: MutableState<MyChoiceDialogUiModel?>,
) {
    viewModel.getViewActionsAsFlow().observeActionsWithLifecycle { action ->
        when (action) {
            QPackInfoViewModel.Action.OpenCardsInBottomList -> Unit // TODO()
            is QPackInfoViewModel.Action.RequestNewCourseCreation -> Unit // TODO()
            is QPackInfoViewModel.Action.RequestsSelectCourseToAdd -> Unit // TODO()
            is QPackInfoViewModel.Action.ShowErrorMessage -> {
                snackbarHostState.showSnackbar(action.message)
            }
            is QPackInfoViewModel.Action.ShowLearnCourseCardsViewingType -> {
                choiceDialogState.value = action.dialogModel
            }
            is NavigationAction -> onNavigationAction(action)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun QPackInfoScreenInternal(
    viewState: State,
    onEvent: (Event) -> Unit,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    MySimpleScreenScaffold(
        isLoaderVisible = viewState.isLoading,
        snackbarHostState = snackbarHostState,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    horizontal = UiOffsets.screenContentHPadding,
                    vertical = UiOffsets.screenContentVPadding
                )
        ) {
            TopAppBar(
                title = { ToolbarTitleView(title = viewState.title, subtitle = null) },
                actions = {
                    MyDropDownMenuBox(
                        menu = viewState.toolbarMenu,
                        onMenuItemClick = {
                            onEvent(Event.ToolbarMenuItemClick(menuItemId = it.id))
                        }
                    ) { onContentClick ->
                        IconButton(onClick = onContentClick) {
                            Icon(Icons.Default.MoreVert, contentDescription = null)
                        }
                    }
                }
            )
            Text(
                text = viewState.cardsCountText,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                style = MaterialTheme.typography.titleSmall
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = UiOffsets.screenContentVPadding)
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(UiOffsets.itemsStandartVOffset, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                viewState.buttons.forEach {
                    MyButton(
                        model = it,
                        onClick = { onEvent(Event.ButtonClick(it.id)) }
                    )
                }
            }
            MySelectableItem(
                model = getIsFavoriteModel(viewState.isFavoriteChecked),
                onClick = { onEvent(Event.FavoriteChange(it.isChecked)) },
                maxLines = 1,
                modifier = Modifier
                    .align(Alignment.End)
            )
        }
    }
}

@Composable
private fun getIsFavoriteModel(isFavoriteChecked: Boolean): MySelectableItemModel {
    val title = stringResource(R.string.qpack_favorites_box).asA()
    return remember(isFavoriteChecked) {
        MySelectableItemModel(
            AnyUiId(),
            title,
            isChecked = isFavoriteChecked,
            isEnabled = true
        )
    }
}

@Preview
@Composable
private fun QPackInfoScreenInternal_Preview() = MyTheme {
    val koin = KoinApplication.init()
        .androidContext(LocalContext.current)
        .modules(
            qPackPresentationModule()
        )
    val mapper = koin.koin.get<QPackInfoButtonsMapper>()

    QPackInfoScreenInternal(
        viewState = State(
            isLoading = false,
            title = "Заголовок".asA(),
            toolbarMenu = getEmptyMenu(),
            cardsCountText = "2/3".asA(),
            isFavoriteChecked = true,
            buttons = mapper.map(Uncompleted(2, 10)).toImmutableList(),
            fastCards = State.CardsState.NotInit
        ),
        onEvent = { },
        snackbarHostState = remember { SnackbarHostState() }
    )
}