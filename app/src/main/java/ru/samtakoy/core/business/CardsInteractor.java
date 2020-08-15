package ru.samtakoy.core.business;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import ru.samtakoy.core.model.Card;
import ru.samtakoy.core.model.QPack;
import ru.samtakoy.core.model.Theme;

public interface CardsInteractor {

    Completable clearDb();

    Card getCard(Long cardId);
    void setCardNewQuestionText(Long cardId, String text);
    void setCardNewAnswerText(Long cardId, String text);

    boolean hasPackCards(Long qPackId);
    QPack getQPack(Long qPackId);
    void deleteQPack(Long qPackId);
    boolean hasAnyQPack();

    List<Card> getQPackCards(QPack qPack);
    int getQPackCardCount(Long qPackId);

    Long addNewTheme(Long parentThemeId, String title);
    boolean deleteTheme(Long themeId);
    Theme getParentTheme(Long themeId);

    List<Theme> getChildThemes(Long themeId);
    List<QPack> getChildQPacks(Long themeId);


    void saveQPackLastViewDate(Long qPackId, long currentTimeLong, boolean incrementViewCounter);

    Single<List<QPack>> getAllQPacksByLastViewDateAsc();
    Single<List<QPack>> getAllQPacksByCreationDateDesc();


}
