package ru.samtakoy.core.presentation.qpack.di

import dagger.Binds
import dagger.Module
import ru.samtakoy.core.presentation.qpack.info.mapper.FastCardUiModelMapper
import ru.samtakoy.core.presentation.qpack.info.mapper.FastCardUiModelMapperImpl
import ru.samtakoy.core.presentation.qpack.list.mapper.QPackListItemUiModelMapper
import ru.samtakoy.core.presentation.qpack.list.mapper.QPackListItemUiModelMapperImpl

@Module
internal interface QPackPresentationModule {

    @Binds
    fun bindsFastCardUiModelMapper(impl: FastCardUiModelMapperImpl): FastCardUiModelMapper

    @Binds
    fun bindsQPackListItemUiModelMapper(impl: QPackListItemUiModelMapperImpl): QPackListItemUiModelMapper
}