package ru.samtakoy.core.business.events;


import ru.samtakoy.core.database.room.entities.CardEntity;

public class CardUpdateEvent {

    private CardEntity mCard;

    public CardUpdateEvent(CardEntity card) {
        mCard = card;
    }

    public CardEntity getCard() {
        return mCard;
    }
}
