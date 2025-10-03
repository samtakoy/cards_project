package ru.samtakoy.core.domain

import io.reactivex.Flowable
import io.reactivex.Single
import kotlinx.coroutines.flow.Flow
import ru.samtakoy.core.data.local.database.room.entities.CardEntity
import ru.samtakoy.core.data.local.database.room.entities.QPackEntity
import ru.samtakoy.core.data.local.database.room.entities.ThemeEntity
import ru.samtakoy.core.data.local.database.room.entities.other.QPackWithCardIds
import java.util.Date

interface CardsInteractor {
    suspend fun clearDb()

    fun getCardAsFlow(cardId: Long): Flow<CardEntity?>

    fun deleteCardWithRelations(cardId: Long)

    suspend fun setCardNewQuestionText(cardId: Long, text: String)

    suspend fun setCardNewAnswerText(cardId: Long, text: String)

    fun getQPackWithCardIdsAsFlow(qPackId: Long): Flow<QPackWithCardIds>

    fun getQPackRx(qPackId: Long): Flowable<QPackEntity>

    suspend fun getQPack(qPackId: Long): QPackEntity?

    suspend fun deleteQPack(qPackId: Long)

    suspend fun getQPackCards(qPackId: Long): List<CardEntity>

    suspend fun getQPackCardIds(qPackId: Long): List<Long>

    suspend fun updateQPackViewCount(qPackId: Long, currentTime: Date)

    fun addNewTheme(parentThemeId: Long, title: String): Single<ThemeEntity>

    suspend fun deleteTheme(themeId: Long): Boolean

    suspend fun getTheme(themeId: Long): ThemeEntity?

    fun getChildThemesAsFlow(themeId: Long): Flow<List<ThemeEntity>>

    fun getChildQPacksAsFlow(themeId: Long): Flow<List<QPackEntity>>

    fun getAllQPacksByLastViewDateAscAsFlow(
        searchString: String?,
        onlyFavorites: Boolean
    ): Flow<List<QPackEntity>>

    fun getAllQPacksByCreationDateDescAsFlow(
        searchString: String?,
        onlyFavorites: Boolean
    ): Flow<List<QPackEntity>>

    suspend fun getQPacksByIds(ids: List<Long>): List<QPackEntity>

    suspend fun addFakeCard(qPackId: Long)
}
