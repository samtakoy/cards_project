package ru.samtakoy.core.screens.cards.answer

import moxy.InjectViewState
import moxy.MvpPresenter
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import ru.samtakoy.core.business.CardsInteractor
import ru.samtakoy.core.business.events.CardUpdateEvent
import ru.samtakoy.core.model.Card
import ru.samtakoy.core.model.QPack
import ru.samtakoy.core.screens.cards.types.CardViewMode
import javax.inject.Inject

@InjectViewState
class CardAnswerPresenter constructor(
        val cardsInteractor: CardsInteractor,
        val eventBus: EventBus,
        val callbacks: Callbacks,
        qPackId: Long,
        cardId: Long,
        val viewMode: CardViewMode,
        val isLastCard: Boolean
) : MvpPresenter<CardAnswerView>() {


    class Factory @Inject constructor(
            val cardsInteractor: CardsInteractor,
            val eventBus: EventBus
    ){
        fun create(
                callbacks: Callbacks,
                qPackId: Long,
                cardId: Long,
                viewMode: CardViewMode,
                isLastCard: Boolean
        ) = CardAnswerPresenter(cardsInteractor, eventBus, callbacks, qPackId, cardId, viewMode, isLastCard)
    }

    interface Callbacks {
        fun onBackToQuestion()
        fun onWrongAnswer()
        fun onNextCard()
        fun onEditAnswerText()
    }


    private val mQPack: QPack
    private var mCard: Card

    init{

        mQPack = cardsInteractor.getQPack(qPackId)
        mCard = cardsInteractor.getCard(cardId)

        eventBus.register(this)

        viewState.setAnswerText(mCard.answer)
        updateButtonsVisibility()
    }

    override fun onDestroy() {

        eventBus.unregister(this)

        super.onDestroy()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onCardUpdateEvent(event: CardUpdateEvent) {
        if (mCard.id.equals(event.card.id)) {
            mCard = event.card
            viewState.setAnswerText(mCard.answer)
        }
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