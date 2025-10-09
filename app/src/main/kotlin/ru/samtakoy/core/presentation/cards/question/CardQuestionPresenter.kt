package ru.samtakoy.core.presentation.cards.question

// TODO remove
interface CardQuestionPresenter {

    interface Callbacks {
        fun onPrevCard()
        fun onViewAnswer()
        fun onNextCard()
        fun onEditQuestionText()
    }
}