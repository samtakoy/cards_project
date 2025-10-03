package ru.samtakoy.core.presentation.qpack.info.vm

import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import org.apache.commons.lang3.exception.ExceptionUtils
import ru.samtakoy.R
import ru.samtakoy.core.app.ScopeProvider
import ru.samtakoy.core.app.some.Resources
import ru.samtakoy.core.app.utils.asAnnotated
import ru.samtakoy.core.data.local.database.room.entities.other.QPackWithCardIds
import ru.samtakoy.core.domain.CardsInteractor
import ru.samtakoy.core.domain.FavoritesInteractor
import ru.samtakoy.core.domain.NCoursesInteractor
import ru.samtakoy.core.domain.utils.MessageException
import ru.samtakoy.core.presentation.base.viewmodel.BaseViewModelImpl
import ru.samtakoy.core.presentation.log.MyLog
import ru.samtakoy.core.presentation.qpack.info.mapper.FastCardUiModelMapper
import ru.samtakoy.core.presentation.qpack.info.vm.QPackInfoViewModel.Action
import ru.samtakoy.core.presentation.qpack.info.vm.QPackInfoViewModel.Event
import ru.samtakoy.core.presentation.qpack.info.vm.QPackInfoViewModel.NavigationAction
import ru.samtakoy.core.presentation.qpack.info.vm.QPackInfoViewModel.State
import ru.samtakoy.features.views.domain.ViewHistoryInteractor
import ru.samtakoy.features.views.domain.ViewHistoryItem

internal class QPackInfoViewModelImpl(
    private val cardsInteractor: CardsInteractor,
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
    private var mQPack: QPackWithCardIds? = null
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
            cardsInteractor.deleteQPack(qPackId)
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
            sendAction(Action.RequestNewCourseCreation(mQPack!!.qPack.title))
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
            val cards = cardsInteractor.getQPackCards(qPackId)
            viewState = viewState.copy(
                fastCards = State.CardsState.Data(cards.map(cardsMapper::map).toImmutableList())
            )
        }
    }

    private fun onUiAddFakeCard() {
        launchWithLoader {
            cardsInteractor.addFakeCard(qPackId)
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
            cardsInteractor.getQPackWithCardIdsAsFlow(qPackId)
                .distinctUntilChanged(),
            viewHistoryInteractor.getLastViewHistoryItemForAsFlow(qPackId)
                .distinctUntilChanged()
        ) { qPack, lastView ->
            mQPack = qPack
            val cardsCountInUncompleted = lastView?.let { it.todoCardIds + it.viewedCardIds } ?: 0
            viewState = viewState.copy(
                title = qPack.qPack.title.asAnnotated(),
                cardsCountText = resources.getString(R.string.qpack_cards_count, qPack.cardCount).asAnnotated(),
                isFavoriteChecked = qPack.qPack.favorite > 0,
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
        return mQPack != null && mQPack!!.cardCount > 0
    }

    private fun onGetError(t: Throwable): Boolean {
        if (t is MessageException) {
            sendAction(Action.ShowErrorMessage(resources.getString(t.msgId)))
        } else {
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