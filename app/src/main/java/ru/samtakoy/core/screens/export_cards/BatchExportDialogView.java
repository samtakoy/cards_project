package ru.samtakoy.core.screens.export_cards;

import moxy.MvpView;
import moxy.viewstate.strategy.OneExecutionStateStrategy;
import moxy.viewstate.strategy.StateStrategyType;


public interface BatchExportDialogView extends MvpView {


    @StateStrategyType(OneExecutionStateStrategy.class)
    void exitOk();
    @StateStrategyType(OneExecutionStateStrategy.class)
    void exitWithError(int errorTextId);

}
