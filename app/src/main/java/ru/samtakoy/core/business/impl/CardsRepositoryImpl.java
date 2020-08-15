package ru.samtakoy.core.business.impl;

import android.content.Context;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import ru.samtakoy.core.business.CardsRepository;
import ru.samtakoy.core.business.events.CardUpdateEvent;
import ru.samtakoy.core.database.MyDb;
import ru.samtakoy.core.model.Card;
import ru.samtakoy.core.model.QPack;
import ru.samtakoy.core.model.Theme;
import ru.samtakoy.core.screens.log.MyLog;

public class CardsRepositoryImpl implements CardsRepository {

    private QPack mCachedQPack;
    private Context mCtx;
    private EventBus mEventBus;

    public CardsRepositoryImpl(Context ctx, EventBus eventBus){
        mCtx = ctx;
        mEventBus = eventBus;
    }


    @Override
    public Completable clearDb() {
        return Completable.fromCallable(() ->
        {
            // TODO test, for readable database instance closing
            deletePack(0L);
            mCtx.deleteDatabase(MyDb.DB_NAME);
            return true;
        }
        );
    }

    @Override
    public Card getCard(Long cardId) {
        return ContentProviderHelper.getConcreteCard(mCtx.getContentResolver(), cardId);
    }

    @Override
    public void saveCard(Card card) {
        ContentProviderHelper.saveCard(mCtx.getContentResolver(), card);
        mEventBus.post(new CardUpdateEvent(card));
    }

    public QPack getQPack(Long qPackId) {
        // TODO LRU cache?
        if(mCachedQPack != null && mCachedQPack.getId() != qPackId){
            mCachedQPack = null;
        }

        if(mCachedQPack == null){
//Log.e(TAG, "qPAckId == "+qPackId);
            mCachedQPack = ContentProviderHelper.getConcretePack(mCtx.getContentResolver(), qPackId);
        }
        return mCachedQPack;
    }

    public void deletePack(Long qPackId){
        ContentProviderHelper.deletePack(mCtx, qPackId);
    }

    @Override
    public void saveQPack(QPack qPack) {
        ContentProviderHelper.saveQPack(mCtx.getContentResolver(), qPack);
    }

    @Override
    public boolean hasAnyQPack() {
        return ContentProviderHelper.isAnyPackExists(mCtx.getContentResolver());
    }

    @Override
    public List<Card> getQPackCards(QPack qPack) {
        return ContentProviderHelper.getQPackCards(mCtx, qPack.getId());
    }

    @Override
    public int getQPackCardCount(Long qPackId) {
        return ContentProviderHelper.getQPackCardCount(mCtx.getContentResolver(), qPackId);
    }

    @Override
    public List<Card> getQPackCardsWithTags(QPack qPack) {
        return ContentProviderHelper.getQPackCardsWithTags(mCtx, qPack.getId());
    }

    @Override
    public List<Theme> getChildThemes(Long themeId) {
        return ContentProviderHelper.getCurThemes(mCtx, themeId);
    }

    @Override
    public List<QPack> getChildQPacks(Long themeId) {
        return ContentProviderHelper.getCurQPacks(mCtx, themeId);
    }

    @Override
    public Observable<QPack> getAllQPacks() {

        return
        Observable.fromCallable(
                () -> {
                    List<QPack> result = ContentProviderHelper.getAllQPacks(mCtx);
                    MyLog.add("-- qPacks: "+result.size());
                    return result;
                }
        ).flatMap(
                qPacks -> Observable.fromIterable(qPacks)
        );
    }

    @Override
    public Theme getTheme(Long themeId) {
        return ContentProviderHelper.getTheme(mCtx, themeId);
    }

    @Override
    public Long addNewTheme(Long parentThemeId, String title) {
        return ContentProviderHelper.addNewTheme(mCtx.getContentResolver(), parentThemeId, title);
    }

    @Override
    public boolean deleteTheme(Long themeId) {
        return ContentProviderHelper.deleteThemeOnlyUnchecked(mCtx, themeId);
    }

    /*
    @Override
    public Theme getParentTheme(Long themeId) {
        return ContentProviderHelper.getParentTheme(mCtx, themeId);
    }/**/

}
