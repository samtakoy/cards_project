package ru.samtakoy.core.presentation.cards.di

import dagger.Binds
import dagger.Module
import ru.samtakoy.core.presentation.cards.answer.vm.CardAnswerViewModelMapper
import ru.samtakoy.core.presentation.cards.answer.vm.CardAnswerViewModelMapperImpl
import ru.samtakoy.core.presentation.cards.question.vm.CardQuestionViewModelMapper
import ru.samtakoy.core.presentation.cards.question.vm.CardQuestionViewModelMapperImpl
import ru.samtakoy.core.presentation.cards.vm.CardsViewViewStateMapper
import ru.samtakoy.core.presentation.cards.vm.CardsViewViewStateMapperImpl

@Module
internal interface CardsViewPresentationModule {
    @Binds
    fun bindsCardsViewViewStateMapper(impl: CardsViewViewStateMapperImpl): CardsViewViewStateMapper

    @Binds
    fun bindCardQuestionViewModelMapper(impl: CardQuestionViewModelMapperImpl): CardQuestionViewModelMapper

    @Binds
    fun bindCardAnswerViewModelMapper(impl: CardAnswerViewModelMapperImpl): CardAnswerViewModelMapper
}