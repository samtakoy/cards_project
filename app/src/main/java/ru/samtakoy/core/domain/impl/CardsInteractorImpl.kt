package ru.samtakoy.core.domain.impl

import android.content.Context
import io.reactivex.Flowable
import io.reactivex.Single
import kotlinx.coroutines.flow.Flow
import ru.samtakoy.core.data.local.database.room.entities.CardEntity
import ru.samtakoy.core.data.local.database.room.entities.CardEntity.Companion.initNew
import ru.samtakoy.core.data.local.database.room.entities.QPackEntity
import ru.samtakoy.core.data.local.database.room.entities.ThemeEntity
import ru.samtakoy.core.data.local.database.room.entities.other.QPackWithCardIds
import ru.samtakoy.core.domain.CardsInteractor
import ru.samtakoy.core.data.local.reps.CardsRepository
import ru.samtakoy.core.data.local.reps.QPacksRepository
import ru.samtakoy.core.data.local.reps.TagsRepository
import ru.samtakoy.core.data.local.reps.ThemesRepository
import java.util.Date
import java.util.Random
import javax.inject.Inject

class CardsInteractorImpl @Inject constructor() : CardsInteractor {
    @Inject
    internal lateinit var mCtx: Context

    @Inject
    internal lateinit var mTagsRepository: TagsRepository

    @Inject
    internal lateinit var mCardsRepository: CardsRepository

    @Inject
    internal lateinit var mThemesRepository: ThemesRepository

    @Inject
    internal lateinit var mQPacksRepository: QPacksRepository

    override suspend fun clearDb() {
        return mCardsRepository.clearDb()
    }

    override fun getCardAsFlow(cardId: Long): Flow<CardEntity?> {
        return mCardsRepository.getCardAsFlow(cardId)
    }

    override fun deleteCardWithRelations(cardId: Long) {
        mTagsRepository.deleteAllTagsFromCard(cardId)
        mCardsRepository.deleteCard(cardId)
    }

    override suspend fun setCardNewQuestionText(cardId: Long, text: String) {
        mCardsRepository.getCard(cardId)?.copy(
            question = text
        )?.let {
            mCardsRepository.updateCard(it)
        }
    }

    override suspend fun setCardNewAnswerText(cardId: Long, text: String) {
        mCardsRepository.getCard(cardId)?.copy(
            answer = text
        )?.let {
            mCardsRepository.updateCard(it)
        }
    }

    override fun getQPackWithCardIdsAsFlow(qPackId: Long): Flow<QPackWithCardIds> {
        return mQPacksRepository.getQPackWithCardIdsAsFlow(qPackId)
    }

    override fun getQPackRx(qPackId: Long): Flowable<QPackEntity> {
        return mQPacksRepository.getQPackRx(qPackId)
    }

    override suspend fun getQPack(qPackId: Long): QPackEntity? {
        return mQPacksRepository.getQPack(qPackId)
    }

    override suspend fun deleteQPack(qPackId: Long) {
        // TODO transactions
        // TODO придумать, что делать с НЕИСПОЛЬЗУЕМЫМИ тегами карточек, когда удаляется карточка
        // пока связь тег-карточка удаляется каскадно, а теги остаются,
        // получается, надо чистить поштучно
        mCardsRepository.deleteQPackCards(qPackId)
        mQPacksRepository.deletePack(qPackId)
    }

    override suspend fun getQPackCards(qPackId: Long): List<CardEntity> {
        return mCardsRepository.getQPackCards(qPackId)
    }

    override suspend fun getQPackCardIds(qPackId: Long): List<Long> {
        return mCardsRepository.getCardsIdsFromQPack(qPackId)
    }

    override suspend fun updateQPackViewCount(qPackId: Long, currentTime: Date) {
        mQPacksRepository.updateQPackViewCount(qPackId, currentTime)
    }

    override fun addNewTheme(parentThemeId: Long, title: String): Single<ThemeEntity> {
        return Single.fromCallable { mThemesRepository.addNewTheme(parentThemeId, title) }
    }

    // TODO пока удаление, только если тема пустая, работает молча
    override suspend fun deleteTheme(themeId: Long): Boolean {
        if (
            mQPacksRepository.getQPacksFromTheme(themeId).size > 0 ||
            mThemesRepository.getChildThemes(themeId).size > 0
        ) {
            return false
        }
        return mThemesRepository.deleteTheme(themeId)
    }

    override suspend fun getTheme(themeId: Long): ThemeEntity? {
        return mThemesRepository.getTheme(themeId)
    }

    override fun getChildThemesAsFlow(themeId: Long): Flow<List<ThemeEntity>> {
        return mThemesRepository.getChildThemesAsFlow(themeId)
    }

    override fun getChildQPacksAsFlow(themeId: Long): Flow<List<QPackEntity>> {
        return mQPacksRepository.getQPacksFromThemeAsFlow(themeId)
    }

    override fun getAllQPacksByLastViewDateAscAsFlow(
        searchString: String?,
        onlyFavorites: Boolean
    ): Flow<List<QPackEntity>> {
        return if (searchString.isNullOrBlank()) {
            mQPacksRepository.getAllQPacksByLastViewDateAscAsFlow(onlyFavorites = onlyFavorites)
        } else {
            mQPacksRepository.getAllQPacksByLastViewDateAscFilteredAsFlow(searchString, onlyFavorites = onlyFavorites)
        }
    }

    override fun getAllQPacksByCreationDateDescAsFlow(
        searchString: String?,
        onlyFavorites: Boolean
    ): Flow<List<QPackEntity>> {
        return if (searchString.isNullOrBlank()) {
            mQPacksRepository.getAllQPacksByCreationDateDescAsFlow(onlyFavorites = onlyFavorites)
        } else {
            mQPacksRepository.getAllQPacksByCreationDateDescFilteredAsFlow(searchString, onlyFavorites = onlyFavorites)
        }
    }

    override suspend fun getQPacksByIds(ids: List<Long>): List<QPackEntity> {
        return mQPacksRepository.getQPacksByIds(ids)
    }

    override suspend fun addFakeCard(qPackId: Long) {
        val num = Random().nextInt(10000)
        val card = initNew(
            id = 0L,
            qPackId = qPackId,
            question = "fake question $num",
            answer = "fake answer $num",
            aImages = listOf(),
            comment = "comment",
            favorite = 0
        )
        mCardsRepository.addCard(card)
    }
}
