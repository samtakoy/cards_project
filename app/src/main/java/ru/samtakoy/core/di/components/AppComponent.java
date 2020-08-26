package ru.samtakoy.core.di.components;


import javax.inject.Singleton;

import dagger.Component;
import ru.samtakoy.core.api.di.ApiModule;
import ru.samtakoy.core.business.di.CardsModule;
import ru.samtakoy.core.business.di.CoursesModule;
import ru.samtakoy.core.database.room.di.DatabaseModule;
import ru.samtakoy.core.di.modules.AppModule;
import ru.samtakoy.core.navigation.NavigationModule;
import ru.samtakoy.core.screens.MainActivity;
import ru.samtakoy.core.screens.cards.CardsViewFragment;
import ru.samtakoy.core.screens.cards.answer.CardAnswerFragment;
import ru.samtakoy.core.screens.cards.question.CardQuestionFragment;
import ru.samtakoy.core.screens.cards.result.CardsViewResultFragment;
import ru.samtakoy.core.screens.courses.info.CourseInfoActivity;
import ru.samtakoy.core.screens.courses.info.CourseInfoFragment;
import ru.samtakoy.core.screens.courses.list.CoursesListActivity;
import ru.samtakoy.core.screens.courses.list.CoursesListFragment;
import ru.samtakoy.core.screens.courses.select.SelectCourseDialogFragment;
import ru.samtakoy.core.screens.export_cards.BatchExportDialogFragment;
import ru.samtakoy.core.screens.import_cards.BatchImportDialogFragment;
import ru.samtakoy.core.screens.import_cards.ImportPackDialogFragment;
import ru.samtakoy.core.screens.import_cards.ImportZipDialogFragment;
import ru.samtakoy.core.screens.qpack.QPackInfoFragment;
import ru.samtakoy.core.screens.qpacks.QPacksListFragment;
import ru.samtakoy.core.screens.themes.ThemesListFragment;
import ru.samtakoy.core.services.NotificationsPlannerService;
import ru.samtakoy.features.import_export.di.ExportModule;
import ru.samtakoy.features.import_export.di.ImportModule;
import ru.samtakoy.features.settings.ClearDbDialogFragment;
import ru.samtakoy.features.settings.SettingsActivity;

// TODO субкомпоненты для сервисов и для приложения
// TODO доступ к компоненту через синхр сингелтон?

@Component(modules = {
        AppModule.class, DatabaseModule.class, CardsModule.class, CoursesModule.class,
        NavigationModule.class, ExportModule.class, ImportModule.class,
        ApiModule.class
})
@Singleton
public interface AppComponent {

    void inject(QPackInfoFragment f);

    void inject(MainActivity a);

    void inject(ThemesListFragment f);

    void inject(BatchExportDialogFragment f);

    // для навигатора.... TODO еще надо?
    void inject(CoursesListActivity a);
    void inject(CoursesListFragment f);

    // TODO больше не надо (инжекты перенести во фрагмент)???
    void inject(CourseInfoActivity a);
    void inject(CourseInfoFragment f);

    void inject(SettingsActivity a);

    void inject(SelectCourseDialogFragment f);


    // progress dialogs
    void inject(ClearDbDialogFragment f);

    void inject(BatchImportDialogFragment f);

    void inject(ImportPackDialogFragment f);

    void inject(ImportZipDialogFragment f);

    void inject(CardsViewFragment f);

    void inject(CardQuestionFragment f);

    void inject(CardAnswerFragment f);

    void inject(CardsViewResultFragment f);

    void inject(QPacksListFragment f);


    // сервисы
    void inject(NotificationsPlannerService s);

}
