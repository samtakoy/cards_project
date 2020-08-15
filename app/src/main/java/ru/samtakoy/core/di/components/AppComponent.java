package ru.samtakoy.core.di.components;


import org.greenrobot.eventbus.EventBus;

import javax.inject.Singleton;

import dagger.Component;
import ru.samtakoy.core.business.di.CardsModule;
import ru.samtakoy.core.business.di.CoursesModule;
import ru.samtakoy.core.business.di.EventBusModule;
import ru.samtakoy.core.business.di.ImportExportModule;
import ru.samtakoy.core.di.modules.AppModule;
import ru.samtakoy.core.navigation.NavigationModule;
import ru.samtakoy.core.screens.MainActivity;
import ru.samtakoy.core.screens.cards.CardAnswerFragment;
import ru.samtakoy.core.screens.cards.CardQuestionFragment;
import ru.samtakoy.core.screens.cards.CardsViewPresenter;
import ru.samtakoy.core.screens.courses.CourseInfoActivity;
import ru.samtakoy.core.screens.courses.CoursesListActivity;
import ru.samtakoy.core.screens.export_cards.BatchExportCoursesPresenter;
import ru.samtakoy.core.screens.export_cards.BatchExportQPacksPresenter;
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

    // для навигатора....
    void inject(CoursesListActivity a);
    void inject(CourseInfoActivity a);
    void inject(SettingsActivity a);


    void inject(ClearDbDialogFragment f);

    void inject(CardsViewPresenter p);

    void inject(QPacksListPresenter p);

}
