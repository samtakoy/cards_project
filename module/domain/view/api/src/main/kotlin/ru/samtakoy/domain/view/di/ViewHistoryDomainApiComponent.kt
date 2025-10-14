package ru.samtakoy.domain.view.di

import ru.samtakoy.domain.view.ViewHistoryInteractor

interface ViewHistoryDomainApiComponent {
    fun viewHistoryInteractor(): ViewHistoryInteractor
}