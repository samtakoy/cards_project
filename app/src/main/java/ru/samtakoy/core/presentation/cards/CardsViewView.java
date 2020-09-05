package ru.samtakoy.core.presentation.cards;

import moxy.MvpView;
import moxy.viewstate.strategy.AddToEndSingleStrategy;
import moxy.viewstate.strategy.AddToEndSingleTagStrategy;
import moxy.viewstate.strategy.OneExecutionStateStrategy;
import moxy.viewstate.strategy.StateStrategyType;
import ru.samtakoy.core.presentation.cards.types.CardViewMode;

public interface CardsViewView extends MvpView {


    @StateStrategyType(value = AddToEndSingleTagStrategy.class, tag = "changeScreen")
    void switchScreenToCard(Long qPackId, Long cardId, CardViewMode viewMode, boolean onAnswer, CardsViewPresenter.AnimationType aType, boolean lastCard);

    //@StateStrategyType(value = AddToEndSingleTagStrategy.class, tag = "changeScreen")
    //void switchScreenToCard(Long qPackId, Long cardId, CardViewMode viewMode, boolean onAnswer, boolean back, boolean lastCard);
    @StateStrategyType(value = AddToEndSingleTagStrategy.class)
    void showProgress(int viewedCardCount, int totalCardCount, boolean onAnswer);

    @StateStrategyType(value = AddToEndSingleTagStrategy.class, tag = "changeScreen")
    void switchScreenToResults(Long learnCourseId, CardViewMode viewMode, CardsViewPresenter.AnimationType aType);

    @StateStrategyType(value = OneExecutionStateStrategy.class)
    void closeScreen();

    @StateStrategyType(value = OneExecutionStateStrategy.class)
    void showEditTextDialog(String text, boolean question);

    @StateStrategyType(value = AddToEndSingleStrategy.class)
    void showRevertButton(boolean visibility);

    @StateStrategyType(value = OneExecutionStateStrategy.class)
    void showError(int stringId);

    @StateStrategyType(value = AddToEndSingleStrategy.class, tag = "loading")
    void blockScreenOnOperation();

    @StateStrategyType(value = AddToEndSingleStrategy.class, tag = "loading")
    void unblockScreenOnOperation();

}
