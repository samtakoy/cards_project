package ru.samtakoy.core.screens.qpacks;

import java.util.List;

import moxy.MvpView;
import moxy.viewstate.strategy.AddToEndSingleStrategy;
import moxy.viewstate.strategy.AddToEndSingleTagStrategy;
import moxy.viewstate.strategy.OneExecutionStateStrategy;
import moxy.viewstate.strategy.StateStrategyType;
import ru.samtakoy.core.database.room.entities.QPackEntity;

public interface QPacksListView extends MvpView {


    @StateStrategyType(value = AddToEndSingleTagStrategy.class, tag = "QPACKS")
    void setQPacks(List<QPackEntity> items, QPackSortType sortType);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void showPackInfo(QPackEntity qPack);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void updateMenuState(QPackSortType sortType);

    @StateStrategyType(value = OneExecutionStateStrategy.class)
    void showError(int codeResId);
}
