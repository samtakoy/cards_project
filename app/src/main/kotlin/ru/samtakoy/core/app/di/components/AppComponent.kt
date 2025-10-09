package ru.samtakoy.core.app.di.components

import dagger.Component
import ru.samtakoy.core.app.di.modules.ApiModule
import ru.samtakoy.core.app.di.modules.AppModule
import ru.samtakoy.core.app.di.modules.CardsModule
import ru.samtakoy.core.app.di.modules.CoroutinesModule
import ru.samtakoy.core.app.di.modules.CoursesModule
import ru.samtakoy.core.app.di.modules.DatabaseModule
import ru.samtakoy.core.app.di.modules.FavoritesModule
import ru.samtakoy.core.presentation.MainActivity
import ru.samtakoy.core.presentation.cards.CardsViewFragment
import ru.samtakoy.core.presentation.cards.answer.CardAnswerFragment
import ru.samtakoy.core.presentation.cards.di.CardsViewPresentationModule
import ru.samtakoy.core.presentation.cards.question.CardQuestionFragment
import ru.samtakoy.core.presentation.cards.result.CardsViewResultFragment
import ru.samtakoy.core.presentation.courses.info.CourseInfoFragment
import ru.samtakoy.core.presentation.courses.list.CoursesListFragment
import ru.samtakoy.core.presentation.courses.select.SelectCourseDialogFragment
import ru.samtakoy.core.presentation.export_cards.BatchExportDialogFragment
import ru.samtakoy.core.presentation.favorites.onboarding.FavoritesFragment
import ru.samtakoy.core.presentation.favorites.qpacks_with_favs.QPackSelectionFragment
import ru.samtakoy.core.presentation.import_cards.BatchImportDialogFragment
import ru.samtakoy.core.presentation.import_cards.ImportPackDialogFragment
import ru.samtakoy.core.presentation.import_cards.ImportZipDialogFragment
import ru.samtakoy.core.presentation.qpack.di.QPackPresentationModule
import ru.samtakoy.core.presentation.qpack.info.QPackInfoFragment
import ru.samtakoy.core.presentation.qpack.list.QPacksListFragment
import ru.samtakoy.core.presentation.schedule.ScheduleEditFragment
import ru.samtakoy.core.presentation.settings.ClearDbDialogFragment
import ru.samtakoy.core.presentation.themes.ThemesListFragment
import ru.samtakoy.core.presentation.themes.di.ThemesPresentationModule
import ru.samtakoy.core.presentation.widget.WidgetSettingsFragment
import ru.samtakoy.features.import_export.di.ExportModule
import ru.samtakoy.features.import_export.di.ImportModule
import ru.samtakoy.features.notifications.NotificationsPlannerService
import ru.samtakoy.features.views.di.ViewsFeatureModule
import ru.samtakoy.features.views.presentation.history.ViewsHistoryFragment
import javax.inject.Singleton

// TODO субкомпоненты для сервисов и для приложения
// TODO доступ к компоненту через синхр сингелтон?
@Component(
    modules = [
        AppModule::class,
        CoroutinesModule::class,
        DatabaseModule::class,
        CardsModule::class,
        CoursesModule::class,
        ExportModule::class,
        ImportModule::class,
        FavoritesModule::class,
        ViewsFeatureModule::class,
        CardsViewPresentationModule::class,
        ThemesPresentationModule::class,
        QPackPresentationModule::class,
        ApiModule::class
    ]
)
@Singleton
interface AppComponent {
    fun inject(f: QPackInfoFragment)

    fun inject(a: MainActivity)

    fun inject(f: ThemesListFragment)

    fun inject(f: BatchExportDialogFragment)

    fun inject(f: CoursesListFragment)

    fun inject(f: CourseInfoFragment)

    fun inject(f: ScheduleEditFragment)

    fun inject(f: SelectCourseDialogFragment)

    // progress dialogs
    fun inject(f: ClearDbDialogFragment)

    fun inject(f: BatchImportDialogFragment)

    fun inject(f: ImportPackDialogFragment)

    fun inject(f: ImportZipDialogFragment)

    fun inject(f: CardsViewFragment)

    fun inject(f: CardQuestionFragment)

    fun inject(f: CardAnswerFragment)

    fun inject(f: CardsViewResultFragment)

    fun inject(f: QPacksListFragment)

    fun inject(f: QPackSelectionFragment)

    fun inject(f: WidgetSettingsFragment)

    // TODO а этов модуль не вынести? в компонент конкретного модуля?
    // https://developer.android.com/training/dependency-injection/dagger-multi-module?hl=ru
    fun inject(f: FavoritesFragment)
    fun inject(f: ViewsHistoryFragment)

    // сервисы
    fun inject(s: NotificationsPlannerService)
}
