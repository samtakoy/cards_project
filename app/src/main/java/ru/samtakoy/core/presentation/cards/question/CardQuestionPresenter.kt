package ru.samtakoy.core.presentation.cards.question

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import moxy.InjectViewState
import moxy.MvpPresenter
import org.apache.commons.lang3.exception.ExceptionUtils
import ru.samtakoy.R
import ru.samtakoy.core.data.local.database.room.entities.CardEntity
import ru.samtakoy.core.domain.CardsInteractor
import ru.samtakoy.core.presentation.cards.types.CardViewMode
import ru.samtakoy.core.presentation.log.MyLog
import javax.inject.Inject

@InjectViewState
class CardQuestionPresenter(

        val cardsInteractor: CardsInteractor,

        val callbacks: Callbacks,
        cardId: Long,
        val viewMode: CardViewMode,
        val lastCard: Boolean

) : MvpPresenter<CardQuestionView>() {


    interface Callbacks {
        fun onPrevCard()
        fun onViewAnswer()
        fun onNextCard()
        fun onEditQuestionText()
    }


    class Factory @Inject constructor(
            val cardsInteractor: CardsInteractor
    ) {

        fun create(
                callbacks: Callbacks,
                cardId: Long,
                viewMode: CardViewMode,
                lastCard: Boolean
        ) = CardQuestionPresenter(cardsInteractor, callbacks, cardId, viewMode, lastCard)
    }

    private var card: CardEntity? = null
    private val mCompositeDisposable: CompositeDisposable = CompositeDisposable()

    init {

        setButtonsVisibility()

        bindData(cardId)
    }

    private fun bindData(cardId: Long) {

        mCompositeDisposable.add(
                cardsInteractor.getCardRx(cardId)
                        .onBackpressureLatest()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                { t -> onCardUpdate(t) },
                                { t -> onGetError(t) }
                        )
        )

    }

    private fun onGetError(t: Throwable) {
        MyLog.add(ExceptionUtils.getMessage(t))
        viewState.showError(R.string.db_request_err_message)
    }

    private fun onCardUpdate(newCard: CardEntity) {
        card = newCard
        viewState.setQuestionText(newCard.question)
    }

    override fun onDestroy() {
        mCompositeDisposable.dispose()
        super.onDestroy()
    }

    private fun setButtonsVisibility() {

        with(viewState) {
            when (viewMode) {
                CardViewMode.LEARNING -> {

                    setPrevCardButtonVisible(true)
                    setViewAnswerButtonVisible(true)
                    setNextCardButtonVisible(false)
                }
                CardViewMode.REPEATING -> {
                    setPrevCardButtonVisible(true)
                    setViewAnswerButtonVisible(true)
                    setNextCardButtonVisible(false)
                }
                CardViewMode.REPEATING_FAST -> {
                    setPrevCardButtonVisible(false)
                    setViewAnswerButtonVisible(true)
                    setNextCardButtonVisible(true)
                }
            }
        }
    }

    fun onUiPrevCard() = callbacks.onPrevCard()
    fun onUiNextCard() = callbacks.onNextCard()
    fun onUiViewAnswer() = callbacks.onViewAnswer()
    fun onUiEditQuestionText() = callbacks.onEditQuestionText()


}