package ru.samtakoy.core.presentation.cards.types;

public enum CardViewSource {

    // с какой целью инициирован просмотр

    // обычный просмотр карточек
    SIMPLE_VIEW,
    // внеплановое повторение
    // TODO проверить, что реально используется
    EXTRA_REPEATING,
    // запланированное повторение
    //
    ROUTINE_REPEATING

}
