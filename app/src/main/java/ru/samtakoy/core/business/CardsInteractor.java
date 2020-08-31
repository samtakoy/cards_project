package ru.samtakoy.core.business;

import java.util.Date;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import ru.samtakoy.core.database.room.entities.CardEntity;
import ru.samtakoy.core.database.room.entities.QPackEntity;
import ru.samtakoy.core.database.room.entities.ThemeEntity;

public interface CardsInteractor {

    Completable clearDb();

    //CardEntity getCard(Long cardId);

    Flowable<CardEntity> getCardRx(Long cardId);

    void deleteCardWithRelations(Long cardId);

    Completable setCardNewQuestionText(Long cardId, String text);

    Completable setCardNewAnswerText(Long cardId, String text);

    boolean hasPackCards(Long qPackId);

    QPackEntity getQPack(Long qPackId);

    Flowable<QPackEntity> getQPackRx(Long qPackId);

    void deleteQPack(Long qPackId);

    boolean hasAnyQPack();

    Flowable<List<CardEntity>> getQPackCards(Long qPackId);

    int getQPackCardCount(Long qPackId);

    Long addNewTheme(Long parentThemeId, String title);

    boolean deleteTheme(Long themeId);

    ThemeEntity getParentTheme(Long themeId);

    List<ThemeEntity> getChildThemes(Long themeId);

    Flowable<List<ThemeEntity>> getChildThemesRx(Long themeId);

    List<QPackEntity> getChildQPacks(Long themeId);

    Flowable<List<QPackEntity>> getChildQPacksRx(Long themeId);

    void saveQPackLastViewDate(Long qPackId, Date currentTime, boolean incrementViewCounter);

    Flowable<List<QPackEntity>> getAllQPacksByLastViewDateAsc();

    Flowable<List<QPackEntity>> getAllQPacksByCreationDateDesc();


}
