package ru.samtakoy.core.business.impl;

import android.content.Context;

import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import ru.samtakoy.core.business.CardsInteractor;
import ru.samtakoy.core.business.CardsRepository;
import ru.samtakoy.core.business.QPacksRepository;
import ru.samtakoy.core.business.TagsRepository;
import ru.samtakoy.core.business.ThemesRepository;
import ru.samtakoy.core.database.room.entities.CardEntity;
import ru.samtakoy.core.database.room.entities.QPackEntity;
import ru.samtakoy.core.database.room.entities.ThemeEntity;
import ru.samtakoy.core.database.room.entities.other.QPackWithCardIds;

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
    public CardsInteractorImpl(
            /*Context ctx,
            TagsRepository tagsRep,
            CardsRepository cardsRep,
            ThemesRepository themesRep,
            QPacksRepository qPacksRep*/
    ) {
        /*
        mCtx = ctx;
        mTagsRepository = tagsRep;
        mCardsRepository = cardsRep;
        mThemesRepository = themesRep;
        mQPacksRepository = qPacksRep;
         */
    }

    @Override
    public Completable clearDb() {
        return mCardsRepository.clearDb();
    }


    /*
    @Override
    public CardEntity getCard(Long cardId) {
        return mCardsRepository.getCard(cardId);
    }*/

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
    public Completable setCardNewQuestionText(Long cardId, String text) {

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
    public Completable setCardNewAnswerText(Long cardId, String text) {

        return Completable.fromCallable(
                () -> {
                    CardEntity card = mCardsRepository.getCard(cardId);
                    card.setAnswer(text);
                    mCardsRepository.updateCard(card);
                    return true;
                }
        );
    }

    public QPackEntity getQPack(Long qPackId) {
        return mQPacksRepository.getQPack(qPackId);
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

                .deleteQPackCards(qPackId)
                .andThen(
                        mQPacksRepository.deletePack(qPackId)
                );
    }

    @Override
    public boolean hasAnyQPack() {
        return mQPacksRepository.hasAnyQPack();
    }

    @Override
    public Flowable<List<CardEntity>> getQPackCards(Long qPackId) {
        return mCardsRepository.getQPackCards(qPackId);
    }

    /*
    @Override
    public Single<List<Long>> getCardsIdsFromQPack(Long qPackId){
        return mCardsRepository.getCardsIdsFromQPack(qPackId);
    }*/

    @Override
    public Long addNewTheme(Long parentThemeId, String title) {
        return mThemesRepository.addNewTheme(parentThemeId, title);
    }

    // TODO пока удаление, только если тема пустая, работает молча
    @Override
    public boolean deleteTheme(Long themeId) {

        // TODO optimize to count check
        if (mQPacksRepository.getQPacksFromTheme(themeId).size() > 0 || mThemesRepository.getChildThemes(themeId).size() > 0) {
            return false;
        }
        return mThemesRepository.deleteTheme(themeId);
    }

    @Override
    public ThemeEntity getParentTheme(Long themeId) {

        ThemeEntity theme = mThemesRepository.getTheme(themeId);
        if (theme == null) {
            return null;
        }
        ThemeEntity parentTheme = mThemesRepository.getTheme(theme.getParentId());
        return parentTheme;
    }

    /*
    @Override
    public List<ThemeEntity> getChildThemes(Long themeId) {
        return mThemesRepository.getChildThemes(themeId);
    }*/

    /*
    @Override
    public List<QPackEntity> getChildQPacks(Long themeId) {
        return mQPacksRepository.getQPacksFromTheme(themeId);
    }*/

    @Override
    public Flowable<List<ThemeEntity>> getChildThemesRx(Long themeId) {
        return mThemesRepository.getChildThemesRx(themeId);
    }

    @Override
    public Flowable<List<QPackEntity>> getChildQPacksRx(Long themeId) {
        return mQPacksRepository.getQPacksFromThemeRx(themeId);
    }


    @Override
    public void saveQPackLastViewDate(Long qPackId, Date currentTime, boolean incrementViewCounter) {
        QPackEntity qPack = getQPack(qPackId);
        qPack.setLastViewDate(currentTime);
        if (incrementViewCounter) {
            qPack.setViewCount(qPack.getViewCount() + 1);
        }
        mQPacksRepository.updateQPack(qPack);
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
