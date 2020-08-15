package ru.samtakoy.features.settings;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import javax.inject.Inject;

import io.reactivex.Completable;
import ru.samtakoy.R;
import ru.samtakoy.core.MyApp;
import ru.samtakoy.core.business.CardsInteractor;
import ru.samtakoy.core.model.utils.cbuild.CBuilderConst;
import ru.samtakoy.core.navigation.RouterHolder;
import ru.samtakoy.core.navigation.Screens;
import ru.samtakoy.core.screens.MainActivity;
import ru.samtakoy.core.screens.ProgressDialogFragment;

public class ClearDbDialogFragment extends ProgressDialogFragment {


    public static final String TAG = "ClearDbDialogFragment";

    @Inject CardsInteractor mCardsInteractor;

    public static ClearDbDialogFragment newFragment(){
        ClearDbDialogFragment result = new ClearDbDialogFragment();
        return result;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        MyApp.getInstance().getAppComponent().inject(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected Completable createObservable() {
        return mCardsInteractor.clearDb();
    }

    @Override
    protected int getTitleTextId() {
        return R.string.clear_db_title;
    }

    @Override
    protected int getErrorTextId() {
        return R.string.clear_db_error;
    }

    @Override
    protected void onComplete() {
        super.onComplete();

        RouterHolder rh = (RouterHolder)getActivity();
        if(rh != null){
            //rh.getRouter().newRootScreen(new Screens.ThemeListScreen(-1L, ""));
            startActivity(MainActivity.newRootActivity(getContext()));
        }
    }
}
