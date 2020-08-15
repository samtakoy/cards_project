package ru.samtakoy.core.business.events;

import ru.samtakoy.core.model.Card;

public class CardUpdateEvent {

    private Card mCard;

    public CardUpdateEvent(Card card){
        mCard = card;
    }

    public Card getCard() {
        return mCard;
    }
}
