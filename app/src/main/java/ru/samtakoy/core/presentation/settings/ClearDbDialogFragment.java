package ru.samtakoy.core.presentation.settings;

import android.os.Bundle;

import androidx.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

import io.reactivex.Completable;
import ru.samtakoy.R;
import ru.samtakoy.core.MyApp;
import ru.samtakoy.core.domain.CardsInteractor;
import ru.samtakoy.core.presentation.MainActivity;
import ru.samtakoy.core.presentation.RouterHolder;
import ru.samtakoy.core.presentation.progress_dialog.ProgressDialogFragment;
import ru.samtakoy.core.presentation.progress_dialog.ProgressDialogPresenter;

public class ClearDbDialogFragment extends ProgressDialogFragment {

    public static final String TAG = "ClearDbDialogFragment";

    @Inject CardsInteractor mCardsInteractor;

    public static ClearDbDialogFragment newFragment(){
        ClearDbDialogFragment result = new ClearDbDialogFragment();
        return result;
    }

    // TODO во все прогресс диалоги делаем инжект, ради инжекта presenter родительского класса, что неочевидно
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        MyApp.getInstance().getAppComponent().inject(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected ProgressDialogPresenter.WorkerImpl createWorkerImpl() {
        return new ProgressDialogPresenter.WorkerImpl() {
            @NotNull
            @Override
            public Completable createObservable() {
                return mCardsInteractor.clearDb();
            }

            @Override
            public int getTitleTextId() {
                return R.string.clear_db_title;
            }

            @Override
            public int getErrorTextId() {
                return R.string.clear_db_error;
            }

            @Override
            public void onComplete() {
                navigateToBlankRootScreen();
            }
        };
    }

    private void navigateToBlankRootScreen() {
        RouterHolder rh = (RouterHolder) getActivity();
        if (rh != null) {
            //rh.getRouter().newRootScreen(new Screens.ThemeListScreen(-1L, ""));
            startActivity(MainActivity.newRootActivity(getContext()));
        }
    }
}
