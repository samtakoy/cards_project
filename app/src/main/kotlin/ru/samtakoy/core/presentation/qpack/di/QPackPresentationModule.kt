package ru.samtakoy.core.presentation.qpack.di

import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.samtakoy.core.presentation.qpack.info.mapper.FastCardUiModelMapper
import ru.samtakoy.core.presentation.qpack.info.mapper.FastCardUiModelMapperImpl
import ru.samtakoy.core.presentation.qpack.info.vm.QPackInfoViewModelImpl
import ru.samtakoy.core.presentation.qpack.list.mapper.QPackListItemUiModelMapper
import ru.samtakoy.core.presentation.qpack.list.mapper.QPackListItemUiModelMapperImpl
import ru.samtakoy.core.presentation.qpack.list.vm.QPacksListViewModelImpl

internal fun qPackPresentationModule() = module {
    factoryOf(::FastCardUiModelMapperImpl) bind FastCardUiModelMapper::class
    viewModelOf(::QPackInfoViewModelImpl)

    factoryOf(::QPackListItemUiModelMapperImpl) bind QPackListItemUiModelMapper::class
    viewModelOf(::QPacksListViewModelImpl)
}