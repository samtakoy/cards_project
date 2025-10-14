package ru.samtakoy.domain.qpack.di

import ru.samtakoy.domain.qpack.QPackInteractor

interface QPackDomainApiComponent {
    fun qPackInteractor(): QPackInteractor
}