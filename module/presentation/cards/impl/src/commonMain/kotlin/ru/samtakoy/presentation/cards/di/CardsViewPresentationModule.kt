package ru.samtakoy.presentation.cards.di

import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.samtakoy.presentation.cards.CardsViewRoute
import ru.samtakoy.presentation.cards.entry.CardsViewEntryImpl
import ru.samtakoy.presentation.cards.screens.view.vm.CardsViewViewModelImpl
import ru.samtakoy.presentation.cards.screens.view.vm.mapper.AnswerButtonsMapper
import ru.samtakoy.presentation.cards.screens.view.vm.mapper.AnswerButtonsMapperImpl
import ru.samtakoy.presentation.cards.screens.view.vm.mapper.CardContentMapper
import ru.samtakoy.presentation.cards.screens.view.vm.mapper.CardContentMapperImpl
import ru.samtakoy.presentation.cards.screens.view.vm.mapper.CardsViewViewStateMapper
import ru.samtakoy.presentation.cards.screens.view.vm.mapper.CardsViewViewStateMapperImpl
import ru.samtakoy.presentation.cards.screens.view.vm.mapper.QuestionButtonsMapper
import ru.samtakoy.presentation.cards.screens.view.vm.mapper.QuestionButtonsMapperImpl
import ru.samtakoy.presentation.cards.screens.viewresult.vm.CardsViewResultViewModelImpl
import ru.samtakoy.presentation.cards.screens.viewresult.vm.mapper.CardsViewResultMapper
import ru.samtakoy.presentation.cards.screens.viewresult.vm.mapper.CardsViewResultMapperImpl
import ru.samtakoy.presentation.navigation.RootFeatureEntry

fun cardsViewPresentationModule() = module {
    factoryOf(::CardsViewEntryImpl) {
        named<CardsViewRoute>()
        bind<RootFeatureEntry>()
    }

    factoryOf(::CardsViewViewStateMapperImpl) bind CardsViewViewStateMapper::class
    factoryOf(::QuestionButtonsMapperImpl) bind QuestionButtonsMapper::class
    factoryOf(::AnswerButtonsMapperImpl) bind AnswerButtonsMapper::class
    factoryOf(::CardContentMapperImpl) bind CardContentMapper::class
    viewModelOf(::CardsViewViewModelImpl)

    // results
    factoryOf(::CardsViewResultMapperImpl) bind CardsViewResultMapper::class
    viewModelOf(::CardsViewResultViewModelImpl)
}