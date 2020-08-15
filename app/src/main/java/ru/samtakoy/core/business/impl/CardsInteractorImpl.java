package ru.samtakoy.core.business.impl;

import android.content.Context;


import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import ru.samtakoy.core.business.CardsInteractor;
import ru.samtakoy.core.business.CardsRepository;
import ru.samtakoy.core.model.Card;
import ru.samtakoy.core.model.QPack;
import ru.samtakoy.core.model.Theme;

public class CardsInteractorImpl implements CardsInteractor {

    private Context mCtx;
    private CardsRepository mCardsRepository;


    public CardsInteractorImpl(Context ctx, CardsRepository cardsRepository){
        mCtx = ctx;
        mCardsRepository = cardsRepository;
    }

    @Override
    public Completable clearDb() {
        return mCardsRepository.clearDb();
    }

    public Card getCard(Long cardId){
        return mCardsRepository.getCard(cardId);
    }

    @Override
    public void setCardNewQuestionText(Long cardId, String text) {
        Card card = mCardsRepository.getCard(cardId);
        card.setQuestion(text);
        mCardsRepository.saveCard(card);
    }

    @Override
    public void setCardNewAnswerText(Long cardId, String text) {
        Card card = mCardsRepository.getCard(cardId);
        card.setAnswer(text);
        mCardsRepository.saveCard(card);
    }

    public boolean hasPackCards(Long qPackId){
        return ContentProviderHelper.getQPackCardIds(mCtx.getContentResolver(), qPackId).length >  0;
    }

    public QPack getQPack(Long qPackId){
        return mCardsRepository.getQPack(qPackId);
    }

    public void deleteQPack(Long qPackId){
        mCardsRepository.deletePack(qPackId);
    }

    @Override
    public boolean hasAnyQPack() {
        return mCardsRepository.hasAnyQPack();
    }

    @Override
    public List<Card> getQPackCards(QPack qPack) {
        return mCardsRepository.getQPackCards(qPack);
    }

    public int getQPackCardCount(Long qPackId) {
        return  mCardsRepository.getQPackCardCount(qPackId);
    }

    @Override
    public Long addNewTheme(Long parentThemeId, String title) {
        return mCardsRepository.addNewTheme(parentThemeId, title);
    }

    // TODO пока удаление, только если тема пустая, работает молча
    @Override
    public boolean deleteTheme(Long themeId) {

        // TODO optimize to count check
        if(mCardsRepository.getChildQPacks(themeId).size() > 0 || mCardsRepository.getChildThemes(themeId).size()>0){
            return false;
        }
        return mCardsRepository.deleteTheme(themeId);
    }

    @Override
    public Theme getParentTheme(Long themeId) {

        Theme theme = mCardsRepository.getTheme(themeId);
        if(theme == null){
            return null;
        }
        Theme parentTheme = mCardsRepository.getTheme(theme.getParentId());
        return parentTheme;
    }

    @Override
    public List<Theme> getChildThemes(Long themeId) {
        return mCardsRepository.getChildThemes(themeId);
    }

    @Override
    public List<QPack> getChildQPacks(Long themeId) {
        return mCardsRepository.getChildQPacks(themeId);
    }

    @Override
    public void saveQPackLastViewDate(Long qPackId, long currentTimeLong, boolean incrementViewCounter) {
        QPack qPack = getQPack(qPackId);
        qPack.setLastViewDateMillis(currentTimeLong);
        if(incrementViewCounter){
            qPack.setViewCount(qPack.getViewCount()+1);
        }
        mCardsRepository.saveQPack(qPack);
    }

    @Override
    public Single<List<QPack>> getAllQPacksByLastViewDateAsc() {
        return mCardsRepository
                .getAllQPacks()
                .toSortedList(
                        (aQPack, bQPack) -> {
                            long delta = aQPack.getLastViewDateAsLong()-bQPack.getLastViewDateAsLong();
                            if(delta > 0){
                                return 1;
                            }else
                            if(delta < 0){
                                return -1;
                            }
                            return 0;
                        }
        );
    }

    @Override
    public Single<List<QPack>> getAllQPacksByCreationDateDesc() {
        return mCardsRepository
                .getAllQPacks()
                .toSortedList(
                        (aQPack, bQPack) -> {
                            long delta = aQPack.getCreationDateAsLong()-bQPack.getCreationDateAsLong();
                            if(delta > 0){
                                return -1;
                            }else
                            if(delta < 0){
                                return 1;
                            }
                            return 0;
                        }
                );
    }

}
