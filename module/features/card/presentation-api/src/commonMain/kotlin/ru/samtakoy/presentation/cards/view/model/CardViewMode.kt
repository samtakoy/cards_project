package ru.samtakoy.presentation.cards.view.model

import kotlinx.serialization.Serializable

enum class CardViewMode {
    // в каком виде инициирован просмотр
    // обучение - просмотр вопроса, затем ответа
    // вопрос: назад ответ
    // ответ:  назад далее
    LEARNING,

    // тренировка - напоминание выученного и проверка, как хорошо выучил с ведением статистики
    // вопрос: ответ
    // ответ:  ошибся далее
    REPEATING,

    // ускоренная тренировка, при которой просматриваются только вопросы, а ответ можно посомтреть кликом по кнопке
    // вопрос: ответ далее
    // ответ:  ошибся далее
    REPEATING_FAST; /*public boolean isTraining(){
        return this == REPEATING || this == REPEATING_FAST;
    }/ **/
}