package ru.samtakoy.core.presentation.widget

import moxy.InjectViewState
import ru.samtakoy.core.data.local.database.room.entities.QPackEntity
import ru.samtakoy.core.domain.CardsInteractor
import ru.samtakoy.core.presentation.log.MyLog
import ru.samtakoy.core.presentation.qpacks.QPacksListPresenter
import javax.inject.Inject

@InjectViewState
class WidgetSettingsPresenter(
    private val widgetId: Int,
    cardsInteractor: CardsInteractor
) : QPacksListPresenter(cardsInteractor){

    init {
        MyLog.add("widgetId = $widgetId")
    }

    class Factory @Inject constructor() {
        @Inject
        lateinit var mCardsInteractor: CardsInteractor

        fun create(widgetId: Int) = WidgetSettingsPresenter(widgetId, mCardsInteractor)
    }

    override fun onUiPackClick(qPack: QPackEntity?) {
        // todo
    }
}