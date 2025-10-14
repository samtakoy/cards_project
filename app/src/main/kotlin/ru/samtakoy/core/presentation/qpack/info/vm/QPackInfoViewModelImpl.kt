package ru.samtakoy.core.presentation.qpack.info.vm

import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import org.apache.commons.lang3.exception.ExceptionUtils
import ru.samtakoy.R
import ru.samtakoy.common.coroutines.ScopeProvider
import ru.samtakoy.common.resources.Resources
import ru.samtakoy.presentation.utils.asAnnotated
import ru.samtakoy.domain.favorites.FavoritesInteractor
import ru.samtakoy.domain.learncourse.NCoursesInteractor
import ru.samtakoy.presentation.base.viewmodel.BaseViewModelImpl
import ru.samtakoy.common.utils.MyLog
import ru.samtakoy.core.presentation.qpack.info.mapper.FastCardUiModelMapper
import ru.samtakoy.core.presentation.qpack.info.vm.QPackInfoViewModel.Action
import ru.samtakoy.core.presentation.qpack.info.vm.QPackInfoViewModel.Event
import ru.samtakoy.core.presentation.qpack.info.vm.QPackInfoViewModel.NavigationAction
import ru.samtakoy.core.presentation.qpack.info.vm.QPackInfoViewModel.State
import ru.samtakoy.domain.card.CardInteractor
import ru.samtakoy.domain.qpack.QPack
import ru.samtakoy.domain.qpack.QPackInteractor
import ru.samtakoy.domain.view.ViewHistoryInteractor
import ru.samtakoy.domain.view.ViewHistoryItem

internal class QPackInfoViewModelImpl(
    private val cardInteractor: CardInteractor,
    private val qPackInteractor: QPackInteractor,
    private val favoritesInteractor: FavoritesInteractor,
    private val coursesInteractor: NCoursesInteractor,
    private val viewHistoryInteractor: ViewHistoryInteractor,
    private val resources: Resources,
    private val cardsMapper: FastCardUiModelMapper,
    scopeProvider: ScopeProvider,
    private val qPackId: Long
) : BaseViewModelImpl<State, Action, Event>(
    scopeProvider = scopeProvider,
    initialState = State(
        isLoading = false,
        title = "".asAnnotated(),
        cardsCountText = "".asAnnotated(),
        isFavoriteChecked = false,
        uncompletedButton = null,
        fastCards = State.CardsState.NotInit
    )
), QPackInfoViewModel {
    private var mQPack: QPack? = null
    private var isPackEmpty: Boolean = false
    private var lastUncompletedView: ViewHistoryItem? = null

    override fun onInit() {
        super.onInit()
        subscribeData()
    }

    override fun onEvent(event: Event) {
        when (event) {
            is Event.AddCardsToCourseCommit -> onUiAddCardsToCourseCommit(event.courseId)
            Event.AddFakeCard -> onUiAddFakeCard()
            Event.AddToExistsCourse -> onUiAddToExistsCourse()
            Event.AddToNewCourse -> onUiAddToNewCourse()
            Event.CardsFastView -> onUiCardsFastView()
            Event.DeletePack -> onUiDeletePack()
            is Event.NewCourseCommit -> onUiNewCourseCommit(event.courseTitle)
            Event.ShowPackCourses -> onUiShowPackCourses()
            Event.ViewPackCards -> onUiViewPackCards()
            Event.ViewPackCardsInList -> onUiViewPackCardsInList()
            Event.ViewPackCardsOrdered -> onViewPackCards(false)
            Event.ViewPackCardsRandomly -> onViewPackCards(true)
            Event.ViewUncompletedClick -> onUiViewUncompletedClick()
            is Event.FavoriteChange -> onUiFavoriteChange(event.isChecked)
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
        sendAction(Action.ShowLearnCourseCardsViewingType)
    }

    private fun onUiViewUncompletedClick() {
        lastUncompletedView?.let {
            sendAction(NavigationAction.NavigateToCardsView(it.id))
        }
    }

    private fun onUiViewPackCardsInList() {
        sendAction(Action.OpenCardsInBottomList)
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
            sendAction(Action.ShowErrorMessage(resources.getString(R.string.btn_ok)))
        }
    }

    private fun onUiFavoriteChange(isChecked: Boolean) {
        launchCatching(
            onError = ::onGetError
        ) {
            favoritesInteractor.setQPackFavorite(
                qPackId = qPackId,
                favorite = if (isChecked) 1 else 0
            )
        }
    }

    private fun onViewPackCards(shuffleCards: Boolean) {
        launchWithLoader {
            val view = viewHistoryInteractor.addNewViewItemForPack(qPackId = qPackId, shuffleCards = shuffleCards)
            sendAction(NavigationAction.NavigateToCardsView(viewItemId = view.id))
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
            mQPack = qPack
            isPackEmpty = cardIds.isEmpty()
            val cardsCountInUncompleted = lastView?.let { it.todoCardIds + it.viewedCardIds } ?: 0
            viewState = viewState.copy(
                title = qPack.title.asAnnotated(),
                cardsCountText = resources.getString(R.string.qpack_cards_count, cardIds.size).asAnnotated(),
                isFavoriteChecked = qPack.favorite > 0,
                uncompletedButton = if (lastView != null && lastView.todoCardIds.isNotEmpty()) {
                    resources.getString(
                        R.string.qpack_btn_view_uncompleted,
                        "${lastView.viewedCardIds.size}/$cardsCountInUncompleted"
                    ).asAnnotated()
                } else {
                    null
                }
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
        } else */ {
            MyLog.add(ExceptionUtils.getMessage(t), t)
            sendAction(Action.ShowErrorMessage(resources.getString(R.string.db_request_err_message)))
        }
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