package ru.samtakoy.core.screens.qpacks;

import java.util.List;

import moxy.MvpView;
import moxy.viewstate.strategy.AddToEndSingleStrategy;
import moxy.viewstate.strategy.AddToEndSingleTagStrategy;
import moxy.viewstate.strategy.OneExecutionStateStrategy;
import moxy.viewstate.strategy.StateStrategyType;
import ru.samtakoy.core.model.QPack;

public interface QPacksListView extends MvpView {


    @StateStrategyType(value = AddToEndSingleTagStrategy.class, tag = "QPACKS")
    void setQPacks(List<QPack> items, QPackSortType sortType);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void showPackInfo(QPack qPack);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void updateMenuState(QPackSortType sortType);
}
