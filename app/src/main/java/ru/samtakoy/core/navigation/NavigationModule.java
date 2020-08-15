package ru.samtakoy.core.navigation;

import dagger.Module;
import dagger.Provides;
import ru.terrakok.cicerone.Cicerone;
import ru.terrakok.cicerone.NavigatorHolder;
import ru.terrakok.cicerone.Router;

@Module
public class NavigationModule {

    private Cicerone<Router> mCicerone;

    public NavigationModule() {
        mCicerone = Cicerone.create();
    }


    @Provides
    public Router provideRouter(){
        return mCicerone.getRouter();
    }

    @Provides
    public NavigatorHolder provideNavigatorHolder(){
        return mCicerone.getNavigatorHolder();
    }

}
