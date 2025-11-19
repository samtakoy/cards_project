package ru.samtakoy.platform.speech

import android.content.Context
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager

internal class PlayCardsTaskImpl(
    private val context: Context
) : PlayCardsTask {

    override suspend fun start(cardIds: List<Long>, onlyQuestions: Boolean) {
        val workManager = WorkManager.getInstance(context)

        val workRequest = OneTimeWorkRequestBuilder<PlayCardsWorker>()
            .setInputData(
                Data.Builder()
                    .putLongArray(
                        PlayCardsWorker.Companion.PARAM_CARD_IDS,
                        cardIds.toLongArray()
                    )
                    .putBoolean(
                        PlayCardsWorker.Companion.PARAM_ONLY_QUESTIONS,
                        onlyQuestions
                    )
                    .build()
            )
            .build()
        workManager
            .enqueueUniqueWork(
                UNIQUE_NAME,
                ExistingWorkPolicy.REPLACE,
                workRequest
            )
    }

    override fun stop() {
        WorkManager.getInstance(context)
            .cancelUniqueWork(UNIQUE_NAME)
    }

    companion object {
        private const val UNIQUE_NAME = "PlayCardsTaskAndroidImpl"
    }
}