package ru.samtakoy.domain.di

import ru.samtakoy.domain.card.CardInteractor
import ru.samtakoy.domain.cardtag.TagInteractor

interface CardDomainApiComponent {
    fun cardInteractor(): CardInteractor
    fun tagInteractor(): TagInteractor
}