package ru.samtakoy.core.presentation.cards.answer

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import moxy.InjectViewState
import moxy.MvpPresenter
import org.apache.commons.lang3.exception.ExceptionUtils
import ru.samtakoy.R
import ru.samtakoy.core.business.CardsInteractor
import ru.samtakoy.core.database.room.entities.CardEntity
import ru.samtakoy.core.presentation.cards.types.CardViewMode
import ru.samtakoy.core.presentation.log.MyLog
import javax.inject.Inject

@InjectViewState
class CardAnswerPresenter constructor(
        val cardsInteractor: CardsInteractor,
        val callbacks: Callbacks,
        qPackId: Long,
        cardId: Long,
        val viewMode: CardViewMode,
        val isLastCard: Boolean
) : MvpPresenter<CardAnswerView>() {


    class Factory @Inject constructor(
            val cardsInteractor: CardsInteractor
    ){
        fun create(
                callbacks: Callbacks,
                qPackId: Long,
                cardId: Long,
                viewMode: CardViewMode,
                isLastCard: Boolean
        ) = CardAnswerPresenter(cardsInteractor, callbacks, qPackId, cardId, viewMode, isLastCard)
    }

    interface Callbacks {
        fun onBackToQuestion()
        fun onWrongAnswer()
        fun onNextCard()
        fun onEditAnswerText()
    }


    private var card: CardEntity? = null
    private val mCompositeDisposable: CompositeDisposable = CompositeDisposable()

    init {
        updateButtonsVisibility()
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
        viewState.setAnswerText(newCard.answer)
    }

    override fun onDestroy() {

        mCompositeDisposable.dispose()
        super.onDestroy()
    }

    private fun updateButtonsVisibility() {

        with(viewState) {
            when (viewMode) {
                CardViewMode.LEARNING -> {
                    setBackButtonVisible(true)
                    setWrongButtonVisible(false)
                    setNextCardButtonVisible(true)
                }
                CardViewMode . REPEATING -> {
                    setBackButtonVisible(false)
                    setWrongButtonVisible(true)
                    setNextCardButtonVisible(true)
                }
                CardViewMode.REPEATING_FAST -> {
                    setBackButtonVisible(false)
                    setWrongButtonVisible(true)
                    setNextCardButtonVisible(true)
                }
            }
        }

    }

    fun onUiBackToQuestion() = callbacks.onBackToQuestion()
    fun onUiWrongAnswer() = callbacks.onWrongAnswer()
    fun onUiNextCard() = callbacks.onNextCard()
    fun onUiEditAnswerText() = callbacks.onEditAnswerText()


}