package ru.samtakoy.presentation.qpacks.screens.info.vm

import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import ru.samtakoy.common.coroutines.ScopeProvider
import ru.samtakoy.common.resources.Resources
import ru.samtakoy.common.utils.MyLog
import ru.samtakoy.domain.card.CardInteractor
import ru.samtakoy.domain.favorites.FavoritesInteractor
import ru.samtakoy.domain.learncourse.NCoursesInteractor
import ru.samtakoy.domain.qpack.QPack
import ru.samtakoy.domain.qpack.QPackInteractor
import ru.samtakoy.domain.view.ViewHistoryInteractor
import ru.samtakoy.domain.view.ViewHistoryItem
import ru.samtakoy.presentation.base.viewmodel.BaseViewModelImpl
import ru.samtakoy.presentation.core.design_system.base.model.UiId
import ru.samtakoy.presentation.qpacks.impl.R
import ru.samtakoy.presentation.qpacks.screens.fastlist.mapper.FastCardUiModelMapper
import ru.samtakoy.presentation.qpacks.screens.info.mapper.QPackInfoButtonsMapper
import ru.samtakoy.presentation.qpacks.screens.info.mapper.QPackInfoDialogMapper
import ru.samtakoy.presentation.qpacks.screens.info.mapper.QPackInfoMenuMapper
import ru.samtakoy.presentation.qpacks.screens.info.vm.QPackInfoViewModel.Action
import ru.samtakoy.presentation.qpacks.screens.info.vm.QPackInfoViewModel.Event
import ru.samtakoy.presentation.qpacks.screens.info.vm.QPackInfoViewModel.NavigationAction
import ru.samtakoy.presentation.qpacks.screens.info.vm.QPackInfoViewModel.State
import ru.samtakoy.presentation.utils.asAnnotated

internal class QPackInfoViewModelImpl(
    private val cardInteractor: CardInteractor,
    private val qPackInteractor: QPackInteractor,
    private val favoritesInteractor: FavoritesInteractor,
    private val coursesInteractor: NCoursesInteractor,
    private val viewHistoryInteractor: ViewHistoryInteractor,
    private val resources: Resources,
    private val cardsMapper: FastCardUiModelMapper,
    private val buttonsMapper: QPackInfoButtonsMapper,
    private val choiceDialogMapper: QPackInfoDialogMapper,
    private val toolbarMenuMapper: QPackInfoMenuMapper,
    scopeProvider: ScopeProvider,
    private val qPackId: Long
) : BaseViewModelImpl<State, Action, Event>(
    scopeProvider = scopeProvider,
    initialState = State(
        isLoading = false,
        title = "".asAnnotated(),
        toolbarMenu = toolbarMenuMapper.map(),
        cardsCountText = "".asAnnotated(),
        isFavoriteChecked = false,
        buttons = buttonsMapper.map(null).toImmutableList(),
        fastCards = State.CardsState.NotInit
    )
), QPackInfoViewModel {
    private var mQPack: QPack? = null
    private var isPackEmpty: Boolean = false
    private var lastUncompletedView: ViewHistoryItem? = null

    init {
        subscribeData()
    }

    override fun onEvent(event: Event) {
        when (event) {
            is Event.AddCardsToCourseCommit -> onUiAddCardsToCourseCommit(event.courseId)
            Event.CardsFastView -> onUiCardsFastView()
            is Event.NewCourseCommit -> onUiNewCourseCommit(event.courseTitle)
            is Event.ViewTypeCommit -> onUiViewTypeCommit(event.itemId)
            is Event.FavoriteChange -> onUiFavoriteChange(event.wasChecked)
            is Event.ButtonClick -> onUiButtonClick(event.btnId)
            is Event.ToolbarMenuItemClick -> onUiToolbarMenuItemClick(event.menuItemId)
        }
    }

    private fun onUiToolbarMenuItemClick(itemId: UiId) {
        when (itemId) {
            QPackInfoMenuMapper.IdItemDeletePack -> onUiDeletePack()
            QPackInfoMenuMapper.IdItemAddFakeCard -> onUiAddFakeCard()
        }
    }

    private fun onUiViewTypeCommit(selectedId: UiId) {
        when (selectedId) {
            QPackInfoDialogMapper.IdItemOrdered -> onUiViewPackCards(shuffleCards = false)
            QPackInfoDialogMapper.IdItemRandomly -> onUiViewPackCards(shuffleCards = true)
            QPackInfoDialogMapper.IdItemInList -> onUiViewPackCardsInList()
        }
    }

    private fun onUiViewPackCardsInList() {
        sendAction(Action.OpenCardsInBottomList)
    }

    private fun onUiViewPackCards(shuffleCards: Boolean) {
        launchWithLoader {
            val view = viewHistoryInteractor.addNewViewItemForPack(qPackId = qPackId, shuffleCards = shuffleCards)
            sendAction(NavigationAction.NavigateToCardsView(viewItemId = view.id))
        }
    }

    private fun onUiButtonClick(id: UiId) {
        when (id) {
            QPackInfoButtonsMapper.IdBtnViewCards -> onUiViewPackCards()
            QPackInfoButtonsMapper.IdBtnViewUncompleted -> onUiViewUncompletedClick()
            QPackInfoButtonsMapper.IdBtnAddToNewCourse -> onUiAddToNewCourse()
            QPackInfoButtonsMapper.IdBtnAddToCourse -> onUiAddToExistsCourse()
            QPackInfoButtonsMapper.IdBtnViewCourses -> onUiShowPackCourses()
        }
    }

    private fun onUiDeletePack() {
        // TODO вынести куда-то контроль комплексного удаления
        launchWithLoader {
            coursesInteractor.deleteQPackCourses(qPackId)
            qPackInteractor.deleteQPack(qPackId)
            sendAction(NavigationAction.CloseScreen)
        }
    }

    private fun onUiNewCourseCommit(courseTitle: String) {
        launchWithLoader {
            val course = coursesInteractor.addCourseForQPack(courseTitle, qPackId)
            sendAction(NavigationAction.ShowCourseScreen(course.id))
        }
    }

    private fun onUiShowPackCourses() {
        sendAction(NavigationAction.NavigateToPackCourses(qPackId))
    }

    private fun onUiAddToNewCourse() {
        if (!hasPackCards()) {
            sendAction(Action.ShowErrorMessage(resources.getString(R.string.msg_there_is_no_cards_in_pack)))
        } else {
            sendAction(Action.RequestNewCourseCreation(mQPack!!.title))
        }
    }

    private fun onUiAddToExistsCourse() {
        if (!hasPackCards()) {
            sendAction(Action.ShowErrorMessage(resources.getString(R.string.msg_there_is_no_cards_in_pack)))
        } else {
            sendAction(Action.RequestsSelectCourseToAdd(qPackId))
        }
    }

    private fun onUiAddCardsToCourseCommit(courseId: Long) {
        launchWithLoader {
            coursesInteractor.onAddCardsToCourseFromQPack(qPackId, courseId)
            sendAction(NavigationAction.ShowCourseScreen(courseId))
        }
    }

    private fun onUiViewPackCards() {
        sendAction(
            Action.ShowLearnCourseCardsViewingType(
                dialogModel = choiceDialogMapper.mapViewTypeDialog()
            )
        )
    }

    private fun onUiViewUncompletedClick() {
        lastUncompletedView?.let {
            sendAction(NavigationAction.NavigateToCardsView(it.id))
        }
    }

    private fun onUiCardsFastView() {
        launchWithLoader {
            val cards = cardInteractor.getQPackCards(qPackId)
            viewState = viewState.copy(
                fastCards = State.CardsState.Data(cards.map(cardsMapper::map).toImmutableList())
            )
        }
    }

    private fun onUiAddFakeCard() {
        launchWithLoader {
            cardInteractor.addFakeCard(qPackId)
            sendAction(
                Action.ShowErrorMessage(
                    resources.getString(ru.samtakoy.common.utils.R.string.action_ok)
                )
            )
        }
    }

    private fun onUiFavoriteChange(wasChecked: Boolean) {
        launchCatching(
            onError = ::onGetError
        ) {
            favoritesInteractor.setQPackFavorite(
                qPackId = qPackId,
                favorite = if (wasChecked) 0 else 1
            )
        }
    }

    private fun subscribeData() {
        combine(
            qPackInteractor.getQPackAsFlow(qPackId)
                .distinctUntilChanged(),
            cardInteractor.getQPackCardIdsAsFlow(qPackId)
                .distinctUntilChanged(),
            viewHistoryInteractor.getLastViewHistoryItemForAsFlow(qPackId)
                .distinctUntilChanged()
        ) { qPack, cardIds, lastView ->
            mQPack = qPack ?: return@combine
            isPackEmpty = cardIds.isEmpty()
            val cardsCountInUncompleted = lastView?.let { it.todoCardIds.size + it.viewedCardIds.size } ?: 0
            viewState = viewState.copy(
                title = qPack.title.asAnnotated(),
                cardsCountText = resources.getString(R.string.qpack_cards_count, cardIds.size).asAnnotated(),
                isFavoriteChecked = qPack.favorite > 0,
                buttons = if (lastView != null && lastView.todoCardIds.isNotEmpty()) {
                    buttonsMapper.map(
                        QPackInfoButtonsMapper.Uncompleted(lastView.viewedCardIds.size, cardsCountInUncompleted)
                    )
                } else {
                    buttonsMapper.map(null)
                }.toImmutableList()
            )
        }.launchIn(mainScope)
    }

    private fun hasPackCards(): Boolean {
        return isPackEmpty.not()
    }

    private fun onGetError(t: Throwable): Boolean {
        /* if (t is MessageException) {
            // TODO
            sendAction(Action.ShowErrorMessage(resources.getString(t.msgId)))
        } else */
            MyLog.add(t.message.orEmpty(), t)
            sendAction(
                Action.ShowErrorMessage(
                    resources.getString(ru.samtakoy.common.utils.R.string.db_request_err_message)
                )
            )
        return true
    }

    private fun launchWithLoader(
        onError: (suspend (Throwable) -> Unit)? = ::onGetError,
        block: suspend () -> Unit
    ) {
        viewState = viewState.copy(isLoading = true)
        launchCatching(
            onError = onError,
            onFinally = { viewState = viewState.copy(isLoading = false) }
        ) {
            block()
        }
    }
}