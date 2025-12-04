package ru.samtakoy.speech.domain.mapper

import org.jetbrains.compose.resources.getString
import ru.samtakoy.resources.Res
import ru.samtakoy.resources.player_answer_audio_title
import ru.samtakoy.resources.player_audio_ending_title
import ru.samtakoy.resources.player_question_audio_last_title
import ru.samtakoy.resources.player_question_audio_title
import ru.samtakoy.resources.player_question_audio_title_with_total
import ru.samtakoy.speech.domain.model.SpeechPlayerState

internal interface PlayerAudioMapper {
    /** Состояние проигрывателя маппит на список текстов для проигрывания */
    suspend fun mapCardAudio(state: SpeechPlayerState.Active, onlyQuestions: Boolean): List<String>
}

internal class PlayerAudioMapperImpl : PlayerAudioMapper {

    override suspend fun mapCardAudio(state: SpeechPlayerState.Active, onlyQuestions: Boolean): List<String> {
        return if (state.curRecord.isQuestion) {
            mapQuestionAudio(
                cardNum = state.curRecord.cardNum,
                cardsTotal = state.cardIds.size,
                cardContent = state.curRecord.textContent,
                onlyQuestions = onlyQuestions
            )
        } else {
            mapAnswerAudio(
                cardNum = state.curRecord.cardNum,
                cardsTotal = state.cardIds.size,
                cardContent = state.curRecord.textContent,
            )
        }
    }

    private suspend fun mapQuestionAudio(
        cardNum: Int,
        cardsTotal: Int,
        cardContent: String,
        onlyQuestions: Boolean
    ): List<String> = buildList {
        if (cardNum == cardsTotal) {
            add(getString(Res.string.player_question_audio_last_title, cardNum))
        } else if (cardNum % 10 == 0) {
            add(getString(Res.string.player_question_audio_title_with_total, cardNum, cardsTotal))
        } else {
            add(getString(Res.string.player_question_audio_title, cardNum))
        }
        add(cardContent)
        if (cardNum == cardsTotal && onlyQuestions) {
            add(getString(Res.string.player_audio_ending_title))
        }
    }

    private suspend fun mapAnswerAudio(
        cardNum: Int,
        cardsTotal: Int,
        cardContent: String
    ): List<String> = buildList {
        add(getString(Res.string.player_answer_audio_title, cardNum))
        add(cardContent)
        if (cardNum == cardsTotal) {
            add(getString(Res.string.player_audio_ending_title))
        }
    }
}
