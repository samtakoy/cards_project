package ru.samtakoy.presentation.themes.di

import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.samtakoy.presentation.navigation.MainTabFeatureEntry
import ru.samtakoy.presentation.themes.list.ThemeListRoute
import ru.samtakoy.presentation.themes.entry.ThemeListEntryImpl
import ru.samtakoy.presentation.themes.list.mapper.ThemeUiItemMapper
import ru.samtakoy.presentation.themes.list.mapper.ThemeUiItemMapperImpl
import ru.samtakoy.presentation.themes.list.mapper.ThemeListMenuItemsMapper
import ru.samtakoy.presentation.themes.list.mapper.ThemeListMenuItemsMapperImpl
import ru.samtakoy.presentation.themes.list.vm.ThemeListViewModelImpl

fun themesPresentationModule() = module {
    factory<MainTabFeatureEntry>(named<ThemeListRoute>()) { ThemeListEntryImpl() }

    factoryOf(::ThemeUiItemMapperImpl) bind ThemeUiItemMapper::class
    factoryOf(::ThemeListMenuItemsMapperImpl) bind ThemeListMenuItemsMapper::class
    viewModel { (themeId: Long, themeTitle: String?) ->
        ThemeListViewModelImpl(
            qPackInteractor = get(),
            themeInteractor = get(),
            permissionsController = get(),
            downloadFileUseCase = get(),
            importCardsFromZipTask = get(),
            uiItemsMapper = get(),
            menuItemMapper = get(),
            savedStateHandle = get(),
            scopeProvider = get(),
            themeId = themeId,
            themeTitle = themeTitle
        )
    }
}