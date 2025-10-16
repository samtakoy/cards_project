package ru.samtakoy.core.presentation.cards.di

import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.samtakoy.core.presentation.cards.answer.vm.CardAnswerViewModelImpl
import ru.samtakoy.core.presentation.cards.answer.vm.CardAnswerViewModelMapper
import ru.samtakoy.core.presentation.cards.answer.vm.CardAnswerViewModelMapperImpl
import ru.samtakoy.core.presentation.cards.question.vm.CardQuestionViewModelImpl
import ru.samtakoy.core.presentation.cards.question.vm.CardQuestionViewModelMapper
import ru.samtakoy.core.presentation.cards.question.vm.CardQuestionViewModelMapperImpl
import ru.samtakoy.core.presentation.cards.result.vm.CardsViewResultViewModelImpl
import ru.samtakoy.core.presentation.cards.vm.CardsViewViewModelImpl
import ru.samtakoy.core.presentation.cards.vm.CardsViewViewStateMapper
import ru.samtakoy.core.presentation.cards.vm.CardsViewViewStateMapperImpl

internal fun cardsViewPresentationModule() = module {
    factoryOf(::CardQuestionViewModelMapperImpl) bind CardQuestionViewModelMapper::class
    viewModelOf(::CardQuestionViewModelImpl)

    viewModelOf(::CardsViewResultViewModelImpl)

    factoryOf(::CardsViewViewStateMapperImpl) bind CardsViewViewStateMapper::class
    viewModelOf(::CardsViewViewModelImpl)

    factoryOf(::CardAnswerViewModelMapperImpl) bind CardAnswerViewModelMapper::class
    viewModelOf(::CardAnswerViewModelImpl)
}