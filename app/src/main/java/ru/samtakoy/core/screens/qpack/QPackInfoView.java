package ru.samtakoy.core.screens.qpack;


import androidx.annotation.Nullable;

import java.util.List;

import moxy.MvpView;
import moxy.viewstate.strategy.AddToEndSingleStrategy;
import moxy.viewstate.strategy.OneExecutionStateStrategy;
import moxy.viewstate.strategy.SingleStateStrategy;
import moxy.viewstate.strategy.StateStrategyType;
import ru.samtakoy.core.database.room.entities.CardEntity;

@StateStrategyType(OneExecutionStateStrategy.class)
public interface QPackInfoView extends MvpView {

    @StateStrategyType(AddToEndSingleStrategy.class)
    void initView(String title, String cardsCount);

    void showCourseScreen(Long courseId);

    void showMessage(int messageId);

    @StateStrategyType(SingleStateStrategy.class)
    void closeScreen();

    void showCourses(Long qPackId);

    void requestNewCourseCreation(String title);

    void requestsSelectCourseToAdd(@Nullable Long qPackId);

    void showLearnCourseCardsViewingType();
    void showLearnCourseCards(Long learnCourseId);
    void showLearnCourseCardsInList(Long learnCourseId);

    void setFastViewCards(List<CardEntity> cards);
}
