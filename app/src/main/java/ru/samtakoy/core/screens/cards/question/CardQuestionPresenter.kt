package ru.samtakoy.core.screens.cards.question

import moxy.InjectViewState
import moxy.MvpPresenter
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import ru.samtakoy.core.business.CardsInteractor
import ru.samtakoy.core.business.events.CardUpdateEvent
import ru.samtakoy.core.model.Card
import ru.samtakoy.core.screens.cards.types.CardViewMode
import javax.inject.Inject

@InjectViewState
class CardQuestionPresenter(

        cardsInteractor: CardsInteractor,
        val eventBus: EventBus,

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
            val cardsInteractor: CardsInteractor,
            val eventBus: EventBus
    ) {

        fun create(
                callbacks: Callbacks,
                cardId: Long,
                viewMode: CardViewMode,
                lastCard: Boolean
        ) = CardQuestionPresenter(cardsInteractor, eventBus, callbacks, cardId, viewMode, lastCard)
    }

    private var card: Card

    init {

        card = cardsInteractor.getCard(cardId);
        eventBus.register(this)


        viewState.setQuestionText(card.question)
        setButtonsVisibility()
    }

    override fun onDestroy() {

        eventBus.unregister(this)

        super.onDestroy()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onCardUpdateEvent(event: CardUpdateEvent) {
        if (card.getId().equals(event.card.id)) {
            card = event.card

            viewState.setQuestionText(card.question)
        }
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