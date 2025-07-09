package ru.samtakoy.core.domain.impl;

import android.content.Context;

import java.util.List;
import java.util.Random;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;
import ru.samtakoy.core.data.local.database.room.entities.CardEntity;
import ru.samtakoy.core.data.local.database.room.entities.QPackEntity;
import ru.samtakoy.core.data.local.database.room.entities.ThemeEntity;
import ru.samtakoy.core.data.local.database.room.entities.other.QPackWithCardIds;
import ru.samtakoy.core.domain.CardsInteractor;
import ru.samtakoy.core.domain.CardsRepository;
import ru.samtakoy.core.domain.QPacksRepository;
import ru.samtakoy.core.domain.TagsRepository;
import ru.samtakoy.core.domain.ThemesRepository;

public class CardsInteractorImpl implements CardsInteractor {

    @Inject
    Context mCtx;
    @Inject
    TagsRepository mTagsRepository;
    @Inject
    CardsRepository mCardsRepository;
    @Inject
    ThemesRepository mThemesRepository;
    @Inject
    QPacksRepository mQPacksRepository;

    @Inject
    public CardsInteractorImpl() {
    }

    @Override
    public Completable clearDb() {
        return mCardsRepository.clearDb();
    }

    @Override
    public Flowable<CardEntity> getCardRx(Long cardId) {
        return mCardsRepository.getCardRx(cardId);
    }

    @Override
    public void deleteCardWithRelations(Long cardId) {
        mTagsRepository.deleteAllTagsFromCard(cardId);
        mCardsRepository.deleteCard(cardId);
    }

    @Override
    public Completable setCardNewQuestionTextRx(Long cardId, String text) {

        return Completable.fromCallable(
                () -> {
                    CardEntity card = mCardsRepository.getCard(cardId);
                    card.setQuestion(text);
                    mCardsRepository.updateCard(card);
                    return true;
                }
        );
    }

    @Override
    public Completable setCardNewAnswerTextRx(Long cardId, String text) {

        return Completable.fromCallable(
                () -> {
                    CardEntity card = mCardsRepository.getCard(cardId);
                    card.setAnswer(text);
                    mCardsRepository.updateCard(card);
                    return true;
                }
        );
    }

    @Override
    public Flowable<QPackWithCardIds> getQPackWithCardIds(Long qPackId) {
        return mQPacksRepository.getQPackWithCardIdsRx(qPackId);
    }

    @Override
    public Flowable<QPackEntity> getQPackRx(Long qPackId) {
        return mQPacksRepository.getQPackRx(qPackId);
    }

    @Override
    public Completable deleteQPack(Long qPackId) {

        // TODO transactions
        return mCardsRepository
                // TODO придумать, что делать с НЕИСПОЛЬЗУЕМЫМИ тегами карточек, когда удаляется карточка
                // пока связь тег-карточка удаляется каскадно, а теги остаются,
                // получается, надо чистить поштучно
                .deleteQPackCards(qPackId)
                .andThen(
                        mQPacksRepository.deletePack(qPackId)
                );
    }

    @Override
    public Flowable<List<CardEntity>> getQPackCards(Long qPackId) {
        return mCardsRepository.getQPackCards(qPackId);
    }

    @Override
    public Single<ThemeEntity> addNewTheme(Long parentThemeId, String title) {
        return Single.fromCallable(() -> mThemesRepository.addNewTheme(parentThemeId, title));
    }

    // TODO пока удаление, только если тема пустая, работает молча
    @Override
    public Completable deleteTheme(Long themeId) {

        // TODO optimize to count check
        return Completable.fromCallable(
                () -> {
                    if (mQPacksRepository.getQPacksFromTheme(themeId).size() > 0 || mThemesRepository.getChildThemes(themeId).size() > 0) {
                        return false;
                    }
                    return mThemesRepository.deleteTheme(themeId);
                }
        );
    }

    @Override
    public Single<ThemeEntity> getTheme(Long themeId) {
        return mThemesRepository.getThemeRx(themeId);
    }

    @Override
    public Flowable<List<ThemeEntity>> getChildThemesRx(Long themeId) {
        return mThemesRepository.getChildThemesRx(themeId);
    }

    @Override
    public Flowable<List<QPackEntity>> getChildQPacksRx(Long themeId) {
        return mQPacksRepository.getQPacksFromThemeRx(themeId);
    }

    @Override
    public Flowable<List<QPackEntity>> getAllQPacksByLastViewDateAsc() {
        return mQPacksRepository.getAllQPacksByLastViewDateAsc();
    }

    @Override
    public Flowable<List<QPackEntity>> getAllQPacksByCreationDateDesc() {
        return mQPacksRepository.getAllQPacksByCreationDateDesc();
    }

    @Override
    public Completable addFakeCard(Long qPackId) {

        return Completable.fromCallable(() -> {

            int num = new Random().nextInt(10000);

            CardEntity card = CardEntity.Companion.initNew(
                    qPackId, "fake question " + num, "fake answer " + num, "comment"
            );
            mCardsRepository.addCard(card);
            return true;
        });
    }
}
