package ru.samtakoy.presentation.cards.screens.view.model

import kotlinx.serialization.Serializable

@Serializable
internal data class BackupInfo(
    val question: String? = null,
    val answer: String? = null
)

internal fun BackupInfo.hasBackup(onAnswer: Boolean): Boolean {
    if (onAnswer) {
        return isAnswerEmpty().not()
    } else {
        return isQuestionEmpty().not()
    }
}

internal fun BackupInfo.isAnswerEmpty(): Boolean {
    return answer == null
}

internal fun BackupInfo.isQuestionEmpty(): Boolean {
    return question == null
}