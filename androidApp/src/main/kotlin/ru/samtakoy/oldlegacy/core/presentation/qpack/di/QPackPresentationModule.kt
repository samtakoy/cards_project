package ru.samtakoy.oldlegacy.core.presentation.qpack.di

import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.samtakoy.oldlegacy.core.presentation.qpack.list.mapper.QPackListItemUiModelMapper
import ru.samtakoy.oldlegacy.core.presentation.qpack.list.mapper.QPackListItemUiModelMapperImpl
import ru.samtakoy.oldlegacy.core.presentation.qpack.list.vm.QPacksListViewModelImpl

internal fun qPackPresentationModuleFragment() = module {


    // список
    factoryOf(::QPackListItemUiModelMapperImpl) bind QPackListItemUiModelMapper::class
    viewModelOf(::QPacksListViewModelImpl)

    // выбор

}