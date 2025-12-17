package ru.samtakoy.presentation.cards.screens.view.model

enum class CodeType {
    /**
     *  Код из-под автопарсера.
     *  Нет уверенности в правильном вычленении из текста,
     *  поэтому данный код в UI будет пропущен через разметку кода Kotlin,
     *  но сам блок кода не будет выделятся из остального текста.
     *  */
    AutoParsedKotlin,
    Kotlin,
    Swift,
    Text
}