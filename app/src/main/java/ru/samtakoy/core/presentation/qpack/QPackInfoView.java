package ru.samtakoy.core.presentation.qpack;


import androidx.annotation.Nullable;

import java.util.List;

import moxy.MvpView;
import moxy.viewstate.strategy.AddToEndSingleStrategy;
import moxy.viewstate.strategy.OneExecutionStateStrategy;
import moxy.viewstate.strategy.SingleStateStrategy;
import moxy.viewstate.strategy.StateStrategyType;
import ru.samtakoy.core.data.local.database.room.entities.CardEntity;

@StateStrategyType(OneExecutionStateStrategy.class)
public interface QPackInfoView extends MvpView {

    @StateStrategyType(AddToEndSingleStrategy.class)
    void initView(String title, String cardsCount);

    void showCourseScreen(Long courseId);

    void showMessage(int messageId);

    @StateStrategyType(SingleStateStrategy.class)
    void closeScreen();

    void navigateToPackCourses(Long qPackId);

    void requestNewCourseCreation(String title);

    void requestsSelectCourseToAdd(@Nullable Long qPackId);

    void showLearnCourseCardsViewingType();

    void navigateToCardsView(Long learnCourseId);

    void openLearnCourseCardsInList(Long learnCourseId);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void setFastViewCards(List<CardEntity> cards);
}
