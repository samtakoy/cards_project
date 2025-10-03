package ru.samtakoy.core.presentation.cards.answer

// todo remove
interface CardAnswerPresenter {

    interface Callbacks {
        fun onBackToQuestion()
        fun onWrongAnswer()
        fun onNextCard()
        fun onEditAnswerText()
    }

}