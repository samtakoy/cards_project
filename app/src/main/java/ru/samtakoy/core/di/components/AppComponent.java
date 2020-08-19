package ru.samtakoy.core.di.components;


import javax.inject.Singleton;

import dagger.Component;
import ru.samtakoy.core.business.di.CardsModule;
import ru.samtakoy.core.business.di.CoursesModule;
import ru.samtakoy.core.business.di.EventBusModule;
import ru.samtakoy.core.business.di.ImportExportModule;
import ru.samtakoy.core.di.modules.AppModule;
import ru.samtakoy.core.navigation.NavigationModule;
import ru.samtakoy.core.screens.MainActivity;
import ru.samtakoy.core.screens.cards.CardsViewPresenter;
import ru.samtakoy.core.screens.cards.answer.CardAnswerFragment;
import ru.samtakoy.core.screens.cards.question.CardQuestionFragment;
import ru.samtakoy.core.screens.cards.result.CardsViewResultFragment;
import ru.samtakoy.core.screens.courses.info.CourseInfoActivity;
import ru.samtakoy.core.screens.courses.info.CourseInfoFragment;
import ru.samtakoy.core.screens.courses.list.CoursesListActivity;
import ru.samtakoy.core.screens.courses.list.CoursesListFragment;
import ru.samtakoy.core.screens.courses.select.SelectCourseDialogFragment;
import ru.samtakoy.core.screens.export_cards.BatchExportCoursesPresenter;
import ru.samtakoy.core.screens.export_cards.BatchExportQPacksPresenter;
import ru.samtakoy.core.screens.import_cards.BatchImportDialogFragment;
import ru.samtakoy.core.screens.import_cards.ImportPackDialogFragment;
import ru.samtakoy.core.screens.import_cards.ImportZipDialogFragment;
import ru.samtakoy.core.screens.qpack.QPackInfoPresenter;
import ru.samtakoy.core.screens.qpacks.QPacksListPresenter;
import ru.samtakoy.core.screens.themes.ThemesListFragment;
import ru.samtakoy.core.screens.themes.mvp.ThemesListPresenter;
import ru.samtakoy.features.settings.ClearDbDialogFragment;
import ru.samtakoy.features.settings.SettingsActivity;

@Component(modules= {
        AppModule.class, CardsModule.class, CoursesModule.class,
        NavigationModule.class, ImportExportModule.class,
        EventBusModule.class
})
@Singleton
public interface AppComponent {

    //void inject(QPackInfoFragment qPackInfoFragment);
    void inject(QPackInfoPresenter p);


    // TODO временно, подумать
    //void inject(ThemesListActivity a);
    void inject(MainActivity a);

    void inject(ThemesListFragment f);

    void inject(ThemesListPresenter p);

    void inject(BatchExportQPacksPresenter p);

    void inject(BatchExportCoursesPresenter p);

    // для навигатора.... TODO еще надо?
    // для навигатора.... TODO еще надо?
    void inject(CoursesListActivity a);

    void inject(CoursesListFragment f);

    // TODO больше не надо (инжекты перенести во фрагмент)
    void inject(CourseInfoActivity a);

    void inject(CourseInfoFragment f);

    void inject(SettingsActivity a);

    void inject(SelectCourseDialogFragment f);


    // progress dialogs
    void inject(ClearDbDialogFragment f);

    void inject(BatchImportDialogFragment f);

    void inject(ImportPackDialogFragment f);

    void inject(ImportZipDialogFragment f);

    void inject(CardsViewPresenter p);

    void inject(CardQuestionFragment f);

    void inject(CardAnswerFragment f);

    void inject(CardsViewResultFragment f);

    void inject(QPacksListPresenter p);

}
