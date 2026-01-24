package ru.samtakoy.speech.platform.mapper

import org.jetbrains.compose.resources.getString
import ru.samtakoy.resources.Res
import ru.samtakoy.resources.player_answer_text_title
import ru.samtakoy.resources.player_question_text_title
import ru.samtakoy.speech.domain.model.SpeechPlayerState

interface PlayerNotificationTextMapper {
    suspend fun mapBigNotificationTitle(playerState: SpeechPlayerState.Active): String
    suspend fun mapSmallNotificationTitle(playerState: SpeechPlayerState.Active): String
    suspend fun mapBigNotificationDescription(playerState: SpeechPlayerState.Active): String
    suspend fun mapSmallNotificationDescription(playerState: SpeechPlayerState.Active): String
}

class PlayerNotificationTextMapperImpl : PlayerNotificationTextMapper {

    override suspend fun mapBigNotificationTitle(
        playerState: SpeechPlayerState.Active
    ): String {
        return if (playerState.curRecord.isQuestion) {
            mapQuestionTitle(
                cardNum = playerState.curRecord.cardNum,
                cardsTotal = playerState.cardIds.size
            )
        } else {
            mapAnswerTitle(
                cardNum = playerState.curRecord.cardNum,
                cardsTotal = playerState.cardIds.size
            )
        }
    }

    override suspend fun mapSmallNotificationTitle(playerState: SpeechPlayerState.Active): String {
        return "${playerState.curRecord.cardNum}/${playerState.cardIds.size}"
    }

    override suspend fun mapBigNotificationDescription(playerState: SpeechPlayerState.Active): String {
        return playerState.curRecord.textContent
    }

    override suspend fun mapSmallNotificationDescription(playerState: SpeechPlayerState.Active): String {
        return ""
    }

    private suspend fun mapQuestionTitle(cardNum: Int, cardsTotal: Int): String {
        return getString(Res.string.player_question_text_title, cardNum, cardsTotal)
    }

    private suspend fun mapAnswerTitle(cardNum: Int, cardsTotal: Int): String {
        return getString(Res.string.player_answer_text_title, cardNum, cardsTotal)
    }
}