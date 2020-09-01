package ru.samtakoy.core.business;

import java.util.Date;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import ru.samtakoy.core.database.room.entities.CardEntity;
import ru.samtakoy.core.database.room.entities.QPackEntity;
import ru.samtakoy.core.database.room.entities.ThemeEntity;
import ru.samtakoy.core.database.room.entities.other.QPackWithCardIds;

public interface CardsInteractor {

    Completable clearDb();

    Flowable<CardEntity> getCardRx(Long cardId);

    //TODO  -> RxJava
    void deleteCardWithRelations(Long cardId);

    Completable setCardNewQuestionText(Long cardId, String text);

    Completable setCardNewAnswerText(Long cardId, String text);

    //TODO  -> RxJava
    QPackEntity getQPack(Long qPackId);

    Flowable<QPackWithCardIds> getQPackWithCardIds(Long qPackId);

    Flowable<QPackEntity> getQPackRx(Long qPackId);

    Completable deleteQPack(Long qPackId);

    //TODO  -> RxJava
    boolean hasAnyQPack();

    Flowable<List<CardEntity>> getQPackCards(Long qPackId);

    //TODO  -> RxJava
    Long addNewTheme(Long parentThemeId, String title);

    //TODO  -> RxJava
    boolean deleteTheme(Long themeId);

    //TODO  -> RxJava
    ThemeEntity getParentTheme(Long themeId);

    Flowable<List<ThemeEntity>> getChildThemesRx(Long themeId);

    Flowable<List<QPackEntity>> getChildQPacksRx(Long themeId);

    //TODO  -> RxJava
    void saveQPackLastViewDate(Long qPackId, Date currentTime, boolean incrementViewCounter);
    Flowable<List<QPackEntity>> getAllQPacksByLastViewDateAsc();

    Flowable<List<QPackEntity>> getAllQPacksByCreationDateDesc();

    Completable addFakeCard(Long qPackId);
}
