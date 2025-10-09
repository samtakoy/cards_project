package ru.samtakoy.core.presentation.cards.types

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class BackupInfo(
    val question: String? = null,
    val answer: String? = null
) : Parcelable

fun BackupInfo.hasBackup(onAnswer: Boolean): Boolean {
    if (onAnswer) {
        return isAnswerEmpty().not()
    } else {
        return isQuestionEmpty().not()
    }
}

fun BackupInfo.isAnswerEmpty(): Boolean {
    return answer == null
}

fun BackupInfo.isQuestionEmpty(): Boolean {
    return question == null
}