package ru.samtakoy.core.business;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import ru.samtakoy.core.model.Card;
import ru.samtakoy.core.model.QPack;
import ru.samtakoy.core.model.Theme;

public interface CardsRepository {

    Completable clearDb();

    Card getCard(Long cardId);
    void saveCard(Card card);

    QPack getQPack(Long qPackId);
    void deletePack(Long qPackId);
    void saveQPack(QPack qPack);
    boolean hasAnyQPack();
    List<Card> getQPackCards(QPack qPack);
    int getQPackCardCount(Long qPackId);
    List<Card> getQPackCardsWithTags(QPack qPack);

    List<Theme> getChildThemes(Long themeId);
    List<QPack> getChildQPacks(Long themeId);

    Observable<QPack> getAllQPacks();

    Theme getTheme(Long themeId);
    Long addNewTheme(Long parentThemeId, String title);
    boolean deleteTheme(Long themeId);


    //Theme getParentTheme(Long themeId);

}
